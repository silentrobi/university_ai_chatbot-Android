package com.example.karabukaiassistant.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.karabukaiassistant.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import controller.database.RealTimeDatabaseHelperChat;
import controller.helper.Session;
import controller.message.MessageAdapter;
import model.Message;

/* this activity displays the messages and interact with dialogflow */
public class ChatActivity extends AppCompatActivity implements AIListener   {

    private Session session;
    private ImageButton send;
    private EditText console;
    private Message message;
    private ListView messages;
    private ImageButton mic;
    private boolean voiceInput = false;
    private TextToSpeech tts;

    /* configuring API.ai */
    private final AIConfiguration config = new AIConfiguration("52a45c8620c042458a47655916251158",
            AIConfiguration.SupportedLanguages.English,
            AIConfiguration.RecognitionEngine.System);

    private final AIDataService aiDataService = new AIDataService(config);
    private final AIRequest aiRequest = new AIRequest(); // to request/send query

    private ArrayList<Message> messageHistory;
    private MessageAdapter adapter;
    private RealTimeDatabaseHelperChat rt;  // NoSQL firebase database helper class

    private static final int REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        AIService aiService = AIService.getService(this, config);
        aiService.setListener(this);

        console = (EditText) findViewById(R.id.c_console);
        send = (ImageButton) findViewById(R.id.c_send);
        messages = (ListView) findViewById(R.id.messages_view);
        mic = (ImageButton) findViewById(R.id.mic);

        session = new Session("USER", this); //  session create
        rt = new RealTimeDatabaseHelperChat();
        messageHistory = rt.userChatHistory(session.getUserEmail()); // initialize the chathistory to display. messageHistory is ArrayList of Message object.
        adapter = new MessageAdapter(this, messageHistory); // see MessageAdapter class to know what adapter does.
        messages.setAdapter(adapter); // activate the adapter on ListView
        adapter.notifyDataSetChanged();

        tts =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        mic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = console.getText().toString().trim();
                console.getText().clear(); // clear edit text after sending the query message

                if (!text.isEmpty()) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    message = new Message();
                    message.setCategory("send");
                    message.setDate(formatter.format(new Date())); // get current date
                    message.setTimesamp(System.currentTimeMillis());
                    message.setText(text);

                    messageHistory.add(message); // adding new Message object to the chathistory Arraylist
                    adapter.notifyDataSetChanged(); // this will update the UI ListView and show the added new message on ListView
                    query(session.getUserEmail(), message); //sending message to DialogFlow


                }

            }
        });




    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        try {
            startActivityForResult(intent, REQ_CODE);
        } catch (ActivityNotFoundException a) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    if (!text.isEmpty()) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        message = new Message();
                        message.setCategory("send");
                        message.setDate(formatter.format(new Date())); // get current date
                        message.setTimesamp(System.currentTimeMillis());
                        message.setText(text);

                        messageHistory.add(message); // adding new Message object to the chathistory Arraylist
                        adapter.notifyDataSetChanged(); // this will update the UI ListView and show the added new message on ListView
                        query(session.getUserEmail(), message); //sending message to DialogFlow
                        voiceInput = true;


                    }

                }
                break;
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent i;
        switch (item.getItemId()) {
            case R.id.change_password:
                session.setPasswordVerifyEmail(session.getUserEmail());
                i = new Intent(this, NewPasswordActivity.class); // New password menu
                startActivity(i);
                return true;
            case R.id.sign_out:
                session.setUserEmail(""); // destroying user session data
                i = new Intent(this, MainActivity.class); // redirecting to home page
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResult(AIResponse response) {

    }

    public void onStart() {

        super.onStart();


    }
    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    /* */
    public void query(String userEmail, Message message) {

        aiRequest.setQuery(message.getText());
        // adding Message object to Firebase: This step is necessary because when user kill the app
        // then messageHistory needs to hold the updated chat history so that user get all the
        // chat history when relaunching the app.
        rt.addMessage(userEmail, message);

        Task task = new Task(); // Asynchronous Task: It does background work
        task.execute(aiRequest);  // executing the async task

    }

    /*
    AsyncTask is one of the easiest ways to implement parallelism in Android without having
    to deal with more complex methods like Threads. Though it offers a basic level of
    parallelism with the UI thread, it should not be used for longer operations (of, say, not more than 2 seconds).
    doInBackground() is the most important as it is where background computations are performed.
    https://stackoverflow.com/questions/25647881/android-asynctask-example-and-explanation

    * */

    private class Task extends AsyncTask<AIRequest, Void, AIResponse> {

        AIError error;

        // This is run in a background thread
        @Override
        protected AIResponse doInBackground(AIRequest... aiRequests) {

            final AIRequest request = aiRequests[0];


            try {
                final AIResponse response = aiDataService.request(aiRequest);
                return response;
            } catch (AIServiceException e) {
                error = new AIError(e);

            }
            return null;
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(AIResponse response) { // when data is available from dialogflow

            if (response != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Message replyMessage = new Message();
                replyMessage.setText(response.getResult().getFulfillment().getSpeech()); // text result from Api.ai
                replyMessage.setCategory("receive");
                replyMessage.setDate(formatter.format(new Date()));
                replyMessage.setTimesamp(System.currentTimeMillis());
                messageHistory.add(replyMessage); //adding to ArrayList of Message object.
                adapter.notifyDataSetChanged(); // IMPORTANT: this will update the UI ListView and show the added new message on ListView
                rt.addMessage(session.getUserEmail(), replyMessage); // adding  Message obejct to Firebase

                if (voiceInput){
                    tts.speak(replyMessage.getText(), TextToSpeech.QUEUE_ADD, null);
                    voiceInput =false;
                }


            } else {
                onError(error);
            }


        }

    }
}
