package controller.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.karabukaiassistant.R;

import java.util.ArrayList;

import model.Message;

/* An adapter acts as a bridge between an AdapterView and the underlying
data for that view. The adapter provides access to the data items and is
responsible for creating a view for each item in the data set or array.

Adapters are a smart way to connect a View with some kind of data source.
Typically, your view would be a ListView and the data would come in form of a Cursor or Array.
So adapters come as subclasses of CursorAdapter or ArrayAdapter.
* */

public class MessageAdapter extends BaseAdapter  {
        private Context context;
        private static ArrayList<Message> messageList = null;

    public MessageAdapter(Context context, ArrayList<Message> list) {
        // TODO Auto-generated constructor stub
        this.context = context;
        messageList = list;


    }

    public void setMessageList(ArrayList<Message> list){
        messageList= list;

    }
    public void add(Message message) {
        MessageAdapter.messageList.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }
    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) { // position:-> index
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /* getView is resposible for create customr view of each element of arraylist
    * Here two views are defined based on user message (send) and Bot message (receive)
    * */

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();

        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Message message = MessageAdapter.messageList.get(position);

        //First checking null value;
        if ((!message.getCategory().equals(""))) {
            if (message.getCategory().trim().equals("receive")) { // BOT message
                convertView = messageInflater.inflate(R.layout.bot_message, null); // map to bot's message layout xml file
                // setting different views (avater View, name text View, message text View)of bot's message item view
                holder.avatar = (View) convertView.findViewById(R.id.avatar);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
                /*Unlike IDs, tags are not used to identify views. Tags are essentially
                 an extra piece of information that can be associated with a view. They are most often used as a convenience
                 to store data related to views in the views themselves rather than by putting them in a separate structure.
               */
                convertView.setTag(holder);
                holder.name.setText("Karabuk AI Assistant");
                holder.messageBody.setText(message.getText());
                holder.messageBody.setTextIsSelectable(true);


            } else { // User message
                convertView = messageInflater.inflate(R.layout.user_message, null); //// map to user's message layout xml file

                // setting different views (avater View, name text View, message text View)of user's message view
                holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
                convertView.setTag(holder);
                holder.messageBody.setText(message.getText()); // set view
                holder.messageBody.setTextIsSelectable(true);
            }
        }
        return convertView;
    }

    public class MessageViewHolder{ // View Holder class that holds the different views of a message.
        View avatar;
        TextView name;
        TextView messageBody;
    }

}
