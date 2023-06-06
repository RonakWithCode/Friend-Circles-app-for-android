package com.crazyostudio.friendcircle.adapters;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.crazyostudio.friendcircle.R;
import com.crazyostudio.friendcircle.databinding.GroupchatreceiverBinding;
import com.crazyostudio.friendcircle.databinding.GroupimagereceiverBinding;
import com.crazyostudio.friendcircle.databinding.ReceiverBinding;
import com.crazyostudio.friendcircle.databinding.ReceiverImageBinding;
import com.crazyostudio.friendcircle.databinding.ReceivercontactBinding;
import com.crazyostudio.friendcircle.databinding.ReceiverpdfBinding;
import com.crazyostudio.friendcircle.databinding.SanderImageBinding;
import com.crazyostudio.friendcircle.databinding.SanderPdfBinding;
import com.crazyostudio.friendcircle.databinding.SenderBinding;
import com.crazyostudio.friendcircle.model.Chat_Model;
import com.crazyostudio.friendcircle.model.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ChatAdapters extends  RecyclerView.Adapter{
    ArrayList<Chat_Model> ChatModels;
    Context context;
    int SANDER_VIEW_TYPE=1
            ,IMAGE_SANDER_VIEW_TYPE=11
            ,SANDER_PDF_VIEW_TYPE=150;
    int GROUP_IMAGE_RECEIVER_VIEW_TYPE=25
            ,GROUP_RECEIVER_VIEW_TYPE =24
            ,RECEIVER_VIEW_TYPE=2
            ,IMAGE_RECEIVER_VIEW_TYPE=22
            ,RECEIVER_PDF_VIEW_TYPE=151
            ,RECEIVER_CONTACT_VIEW_TYPE=152;

    public ChatAdapters(ArrayList<Chat_Model> chatModels, Context context) {
        ChatModels = chatModels;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==SANDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sender,parent,false);
            return new SanderViewHolder(view);
        }
        else if (viewType==SANDER_PDF_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sander_pdf,parent,false);
            return new SanderPDFViewHolder(view);
        }
        else if (viewType==RECEIVER_PDF_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.receiverpdf,parent,false);
            return new ReceiverPDFViewHolder(view);
        }
        else if (viewType==RECEIVER_CONTACT_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.receivercontact,parent,false);
            return new ReceiverCONTACTViewHolder(view);
        }
        else if (viewType==IMAGE_SANDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sander_image,parent,false);
            return new ImageSanderViewHolder(view);
        }
        else if (viewType==IMAGE_RECEIVER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_image,parent,false);
            return new ImageReceiverViewHolder(view);
        }
        else if (viewType==GROUP_IMAGE_RECEIVER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.groupimagereceiver,parent,false);
            return new ImageGroupReceiverViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
//        SANDER
        if (ChatModels.get(position).getID().equals(FirebaseAuth.getInstance().getUid())) {
            if (ChatModels.get(position).isImage()) {
                return IMAGE_SANDER_VIEW_TYPE;
            } else if (ChatModels.get(position).isPDF()) {
                return SANDER_PDF_VIEW_TYPE;
            } else {
                return SANDER_VIEW_TYPE;
            }
        }
//
        else {
            if (ChatModels.get(position).isGroup()) {
                if (ChatModels.get(position).isImage()) {
                    return GROUP_IMAGE_RECEIVER_VIEW_TYPE;
                }else {
                    return GROUP_RECEIVER_VIEW_TYPE;
                }
            } else if (ChatModels.get(position).isPDF()) {
                return RECEIVER_PDF_VIEW_TYPE;
            } else if (ChatModels.get(position).isContact()) {
                return RECEIVER_CONTACT_VIEW_TYPE;
            } else {
                return RECEIVER_VIEW_TYPE;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat_Model chatModel = ChatModels.get(position);
        if (holder.getClass()==SanderViewHolder.class){
            ((SanderViewHolder)holder).SendBinding.messageText.setText(chatModel.getMessage());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);

            ((SanderViewHolder)holder).SendBinding.messageTime.setText(time);

        }
        else if (holder.getClass() == SanderPDFViewHolder.class) {
            if (chatModel.getFilename().endsWith(".pdf")){
                ((SanderPDFViewHolder)holder).pdfBinding.pdfIcon.setImageResource(R.drawable.pdf);
            }
            else if (chatModel.getFilename().endsWith(".apk")){
                ((SanderPDFViewHolder)holder).pdfBinding.pdfIcon.setImageResource(R.drawable.apk);
            }else {
                ((SanderPDFViewHolder)holder).pdfBinding.pdfIcon.setImageResource(R.drawable.document);

            }
//            ((SanderPDFViewHolder)holder).pdfBinding.filename.setText(chatModel.getMessage());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);

            ((SanderPDFViewHolder)holder).pdfBinding.filename.setText(chatModel.getFilename());
            ((SanderPDFViewHolder)holder).pdfBinding.size.setText(chatModel.getFileSize());
//            ((SanderPDFViewHolder)holder).pdfBinding.pages.setText(chatModel.getFilePage());
            ((SanderPDFViewHolder)holder).pdfBinding.time.setText(time);
            ((SanderPDFViewHolder)holder).pdfBinding.Download.setOnClickListener(view->
            {
                String url = chatModel.getMessage();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, chatModel.getFilename());
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                Toast.makeText(context, "Check Notification Bar or Download Folder ", Toast.LENGTH_SHORT).show();

            });
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//                builder1.setMessage("Choices");
//                builder1.setCancelable(false);

//                builder1.setPositiveButton(
//                        "Save",
//                        (dialog, id) -> {
//                            dialog.cancel();
//                            String url = chatModel.getMessage();
//                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                            request.allowScanningByMediaScanner();
//                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, chatModel.getFilename());
//                            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//                            manager.enqueue(request);
//                        });
//
//                builder1.setNegativeButton(
//                        "Open",
//                        (dialog, id) -> {
////                            Intent intent = new Intent(context, LoadPdf.class);
////                            intent.putExtra("pdf",chatModel.getMessage());
////                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, Uri.parse(chatModel.getMessage()));
//                            String url = chatModel.getMessage();
//                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                            request.allowScanningByMediaScanner();
//                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, chatModel.getFilename());
//                            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//                            manager.enqueue(request);
//
//                            try {
//                                manager.openDownloadedFile(0);
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            }
//                            Intent intent = new Intent(Intent.ACTION_SEND);
//                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                            intent.setType("application/pdf");
//                            intent.putExtra(Intent.EXTRA_STREAM,chatModel.getMessage());
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(Intent.createChooser(,"PDF"));

//                            intent.addCategory(Intent.DO);

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
//                            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, chatModel.getMessage());
//
//                            context.startActivity(intent);
//                            dialog.cancel();
//                AlertDialog alert11 = builder1.create();
//                alert11.show();

//                ProgressDialog dialog;
//                dialog = new ProgressDialog(context);
//                dialog.setTitle("Downloading ... ");

//                dialog.setMessage(""+reference);
//            });
        }
        else if (holder.getClass() == ReceiverPDFViewHolder.class) {
            if (chatModel.getFilename().endsWith(".pdf")){
                ((ReceiverPDFViewHolder)holder).pdfBinding.pdfIcon.setImageResource(R.drawable.pdf);
            }
            else if (chatModel.getFilename().endsWith(".apk")){
                ((ReceiverPDFViewHolder)holder).pdfBinding.pdfIcon.setImageResource(R.drawable.apk);
            }else {
                ((ReceiverPDFViewHolder)holder).pdfBinding.pdfIcon.setImageResource(R.drawable.document);
            }
//            ((SanderPDFViewHolder)holder).pdfBinding.filename.setText(chatModel.getMessage());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);
            ((ReceiverPDFViewHolder)holder).pdfBinding.filename.setText(chatModel.getFilename());
            ((ReceiverPDFViewHolder)holder).pdfBinding.size.setText(chatModel.getFileSize());
//            ((ReceiverPDFViewHolder)holder).pdfBinding.pages.setText(chatModel.getFilePage());
            ((ReceiverPDFViewHolder)holder).pdfBinding.time.setText(time);
            ((ReceiverPDFViewHolder)holder).pdfBinding.Download.setOnClickListener(view->
            {

                String url = chatModel.getMessage();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, chatModel.getFilename());
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                Toast.makeText(context, "Check Notification Bar or Download Folder ", Toast.LENGTH_SHORT).show();

            });
        }
        else if (holder.getClass()==ReceiverCONTACTViewHolder.class)
        {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);
            ((ReceiverCONTACTViewHolder)holder).ContactBinding.time.setText(time);
            ((ReceiverCONTACTViewHolder)holder).ContactBinding.filename.setText(chatModel.getMessage());
            ((ReceiverCONTACTViewHolder)holder).ContactBinding.number.setText(chatModel.getFilename());
            ((ReceiverCONTACTViewHolder)holder).ContactBinding.main.setOnClickListener(view->{
                PopupMenu popup = new PopupMenu(context,  ((ReceiverCONTACTViewHolder)holder).ContactBinding.main);

                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.contactmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.findName:
                            findingUser(chatModel.getMessage(), ((ReceiverCONTACTViewHolder)holder),position);
                            Toast.makeText(context, "find", Toast.LENGTH_SHORT).show();
                            return true;





////            case R.id.delete:
////                delete(item);
//                return true;
                        default:
                            return false;
                    }
                });
                popup.show();

            });
            ((ReceiverCONTACTViewHolder)holder).ContactBinding.add.setOnClickListener(view->{
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                intent.putExtra(ContactsContract.Intents.Insert.NAME, chatModel.getMessage());
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, chatModel.getFilename());

                context.startActivity(intent);
            });
            ((ReceiverCONTACTViewHolder)holder).ContactBinding.call.setOnClickListener(view->{
                Intent phone_intent = new Intent(Intent.ACTION_CALL);
                // Set data of Intent through Uri by parsing phone number

                phone_intent.setData(Uri.parse("tel:" + chatModel.getFilename()));

                // start Intent
                context.startActivity(phone_intent);
            });
        }


        else if (holder.getClass()==ImageSanderViewHolder.class)
        {
            Glide.with(context).load(chatModel.getMessage()).into(((ImageSanderViewHolder)holder).SendBinding.SanderImageview);
            ((ImageSanderViewHolder) holder).SendBinding.SanderImageview.setOnClickListener(view -> {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(chatModel.getMessage())));

            });

        }
        else if (holder.getClass()==ImageGroupReceiverViewHolder.class)
        {
            ((ImageGroupReceiverViewHolder) holder).GroupBinding.SanderName.setText(chatModel.getSanderName());
            Glide.with(context).load(chatModel.getMessage()).into(((ImageGroupReceiverViewHolder)holder).GroupBinding.RImageview);
            ((ImageGroupReceiverViewHolder) holder).GroupBinding.RImageview.setOnClickListener(view -> {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(chatModel.getMessage())));

            });
        }

        else if (holder.getClass()==ImageReceiverViewHolder.class)
        {
            Glide.with(context).load(chatModel.getMessage()).into(((ImageReceiverViewHolder)holder).Binding.Imageview);
            ((ImageReceiverViewHolder) holder).Binding.Imageview.setOnClickListener(view -> context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(chatModel.getMessage()))));


        }
        else if (holder.getClass()==GroupReceiverViewHolder.class)
        {
            ((GroupReceiverViewHolder)holder).GroupBinding.SanderName.setText(chatModel.getSanderName());
            ((GroupReceiverViewHolder)holder).GroupBinding.mas.setText(chatModel.getMessage());
        }
        else {

            ((ReceiverViewHolder)holder).binding.messageText.setText(chatModel.getMessage());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);

            ((ReceiverViewHolder)holder).binding.messageTime.setText(time);
        }

    }

    private void findingUser(String name, ReceiverCONTACTViewHolder view, int position) {
        FirebaseDatabase users;
        FirebaseAuth auth;
        UserInfoAdapters userInfoAdapters;
        auth = FirebaseAuth.getInstance();
        users = FirebaseDatabase.getInstance();
        ArrayList<UserInfo> userInfoS = new ArrayList<>();
        userInfoAdapters = new UserInfoAdapters(userInfoS, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(userInfoAdapters);
        users.getReference().child("UserInfo").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInfoS.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserInfo userInfo = snapshot1.getValue(UserInfo.class);
                    if (!Objects.equals(snapshot1.getKey(), auth.getUid())) {
                        assert userInfo != null;
                        userInfo.setUserid(snapshot1.getKey());
                        userInfoS.add(userInfo);

                    }
                    userInfoAdapters.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return ChatModels.size();
    }

    public static class SanderViewHolder extends RecyclerView.ViewHolder {
        SenderBinding SendBinding;
        public SanderViewHolder(@NonNull View itemView) {
            super(itemView);
            SendBinding = SenderBinding.bind(itemView);
        }
    }
    public static class ImageSanderViewHolder extends RecyclerView.ViewHolder {
        SanderImageBinding SendBinding;
        public ImageSanderViewHolder(@NonNull View itemView) {
            super(itemView);
            SendBinding = SanderImageBinding.bind(itemView);
        }
    }
    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ReceiverBinding binding;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ReceiverBinding.bind(itemView);
        }
    }
    public static class ImageReceiverViewHolder extends RecyclerView.ViewHolder {
        ReceiverImageBinding Binding;
        public ImageReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            Binding = ReceiverImageBinding.bind(itemView);
        }
    }
    public static class GroupReceiverViewHolder extends RecyclerView.ViewHolder {
        GroupchatreceiverBinding GroupBinding;

        public GroupReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            GroupBinding = GroupchatreceiverBinding.bind(itemView);
        }
    }
    public static class ImageGroupReceiverViewHolder extends RecyclerView.ViewHolder {
            GroupimagereceiverBinding GroupBinding;

            public ImageGroupReceiverViewHolder(@NonNull View itemView) {
                super(itemView);
                GroupBinding = GroupimagereceiverBinding.bind(itemView);
            }
    }

    public static class SanderPDFViewHolder extends RecyclerView.ViewHolder {
        SanderPdfBinding pdfBinding;

        public SanderPDFViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfBinding = SanderPdfBinding.bind(itemView);
        }
    }
    public static class ReceiverPDFViewHolder extends RecyclerView.ViewHolder {
        ReceiverpdfBinding pdfBinding;

        public ReceiverPDFViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfBinding = ReceiverpdfBinding.bind(itemView);
        }
    }
    public static class ReceiverCONTACTViewHolder extends RecyclerView.ViewHolder {
        ReceivercontactBinding ContactBinding;

        public ReceiverCONTACTViewHolder(@NonNull View itemView) {
            super(itemView);
            ContactBinding = ReceivercontactBinding.bind(itemView);
        }
    }
}