package com.zasa.superduper.Adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.runtimepermission.RuntimePermission;
import com.google.android.material.button.MaterialButton;
import com.zasa.superduper.Models.Question_Model;
import com.zasa.superduper.MyCallBack;
import com.zasa.superduper.MyFunctions;
import com.zasa.superduper.R;
import com.zasa.superduper.activities.CaptureImageActivity;
import com.zasa.superduper.activities.QuestionActivity;
import com.zasa.superduper.activities.ScannerViewActivity;

import java.util.ArrayList;

public class Question_Adapter extends RecyclerView.Adapter implements Question_Adapters {
    private String TAG = "context";
    private String imagePath = "";
    ArrayList<Question_Model> questionList;
    Context context;
    AdapterShowImages adapterShowImages;
    private final int REQUEST_CAMERA = 1234;
    private final int REQUEST_GALLERY = 5464;
    private MyFunctions myFunctions;
    private MyCallBack myCallBack;

    public Question_Adapter(ArrayList<Question_Model> questionList, Context context, MyCallBack myCallBack) {
        this.questionList = questionList;
        this.context = context;
        myFunctions = new MyFunctions(context);
        this.myCallBack = myCallBack;
    }

    @Override
    public int getItemViewType(int position) {

        if (questionList.get(position).getType().equalsIgnoreCase("text"))
            return 1;
        else if (questionList.get(position).getType().equalsIgnoreCase("picture"))
            return 2;
        else if (questionList.get(position).getType().equalsIgnoreCase("qr"))
            return 3;
        else
            questionList.get(position).getType().equalsIgnoreCase("");

        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.textitem, parent, false);
            return new ViewHolderText(view);
        } else if (viewType == 2) {
            view = layoutInflater.inflate(R.layout.image_item, parent, false);
            return new PicViewHolder(view);
        } else if (viewType == 3) {
            view = layoutInflater.inflate(R.layout.qr_item, parent, false);
            return new ViewHolderQr(view);
        } else
            view = layoutInflater.inflate(R.layout.each_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Question_Model model = questionList.get(position);

//        holder.tv_question.setText(model.getQuestion_name());

        if (questionList.get(position).getType().equalsIgnoreCase("text")) {
            ViewHolderText viewHolderText = (ViewHolderText) holder;
            viewHolderText.txt_cate.setText(model.getQuestion_name());
        } else if (questionList.get(position).getType().equalsIgnoreCase("picture")) {
            PicViewHolder viewHolderPic = (PicViewHolder) holder;
            viewHolderPic.txt_categ.setText(model.getQuestion_name());

            viewHolderPic.btn_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterShowImages = (AdapterShowImages) viewHolderPic.recyclerView.getAdapter();
                    QuestionActivity.selectedRow = adapterShowImages;
                    myCallBack.notify("takepic", "takepic");
//                    getImage();
                }
            });

            adapterShowImages = new AdapterShowImages(context, questionList.get(position).getArrayList(), position);
            viewHolderPic.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));   //ctl+p to know parameters
            viewHolderPic.recyclerView.setAdapter(adapterShowImages);

        } else if (questionList.get(position).getType().equalsIgnoreCase("qr")) {
            ViewHolderQr viewHolderQr = (ViewHolderQr) holder;
            viewHolderQr.txt_catego.setText(model.getQuestion_name());


            viewHolderQr.btn_qr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, ScannerViewActivity.class);
                    ScannerViewActivity.code =viewHolderQr.qr_code;
                    context.startActivity(intent);

                    viewHolderQr.qr_code.setText(questionList.get(position).getQr_code());

                }
            });

            Intent intent = ((Activity) context).getIntent();
            intent.getStringExtra("code");


        } else {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tv_question.setText(model.getQuestion_name());
        }
    }

    private void getImage() {
        final CharSequence[] items;
        try {
            items = new CharSequence[]{"Take Photo", "Choose Image", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setTitle("Select Image");
            builder.setItems(items, (dialogInterface, i) -> {
                if (items[i].equals("Take Photo")) {
                    RuntimePermission.askPermission((FragmentActivity) context)
                            .request(Manifest.permission.CAMERA)
                            .onAccepted(result -> {
                                takePicture();
                            })
                            .onDenied(result -> {
                                new android.app.AlertDialog.Builder(context)
                                        .setMessage("Please accept our permissions")
                                        .setPositiveButton("yes", (dialog1, which) -> result.askAgain()) // ask again
                                        .setNegativeButton("no", (dialog1, which) -> dialog1.dismiss())
                                        .show();
                            })
                            .ask();
                } else if (items[i].equals("Choose Image")) {
                    RuntimePermission.askPermission((FragmentActivity) context)
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .onAccepted(result -> {
                                onClickGallery();
                            })
                            .onDenied(result -> {
                                new android.app.AlertDialog.Builder(context)
                                        .setMessage("Please accept our permissions")
                                        .setPositiveButton("yes", (dialog1, which) -> result.askAgain()) // ask again
                                        .setNegativeButton("no", (dialog1, which) -> dialog1.dismiss())
                                        .show();
                            })
                            .ask();
                } else {
                    dialogInterface.dismiss();
                }
            });
            builder.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, myFunctions.setImageUri());
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            ((Activity) context).startActivityForResult(takePictureIntent, REQUEST_CAMERA);

        } catch (Exception e) {
            Log.d("tag", "takeImageIssue " + e.toString());
        }
    }

    private void onClickGallery() {
        Intent intent = new Intent();
        intent.setType("image/jpg");
        intent.setAction(Intent.ACTION_PICK);
        ((Activity) context).startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            new captureImageAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myFunctions.getImagePath());
        } else if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            new captureImageAsync1().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myFunctions.getPath(data.getData(), context));
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class captureImageAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            imagePath = strings[0];
            try {
                return myFunctions.getRightAngleImage(imagePath);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return imagePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // you can show progress bar here while image loading/fetching
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            getImagePathN(myFunctions.decodeFile(imagePath));

            //Close progress bar
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class captureImageAsync1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            imagePath = strings[0];
            try {
                return myFunctions.getRightAngleImage(imagePath);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return imagePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // you can show progress bar here
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // setImageUsingSwitch(PubFun.decodeFile(imagePath));
            getImagePathN(myFunctions.decodeFile(imagePath));

            //Close progress bar

        }
    }

    private void getImagePathN(Bitmap bitmap) {
        Uri tempUri = myFunctions.getImageUri(context, bitmap);
        String actualImagePath = myFunctions.getRealPathFromURI(tempUri, context);

        Log.d(TAG, "image path : " + actualImagePath);

        adapterShowImages.updateList(actualImagePath);

    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    class ViewHolderText extends RecyclerView.ViewHolder {
        EditText ed_number;
        TextView txt_cate;

        public ViewHolderText(@NonNull View itemView) {
            super(itemView);

            ed_number = itemView.findViewById(R.id.edt_enter_number);
            txt_cate = itemView.findViewById(R.id.txt_cate);
        }
    }

    class PicViewHolder extends RecyclerView.ViewHolder {

        TextView txt_categ;
        MaterialButton btn_iv;
        RecyclerView recyclerView;

        public PicViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_iv = itemView.findViewById(R.id.btn_captureImage);
            txt_categ = itemView.findViewById(R.id.txt_categ);
            recyclerView = itemView.findViewById(R.id.item_images_rcv);
        }
    }

    class ViewHolderQr extends RecyclerView.ViewHolder {
        Button btn_qr;
        TextView txt_catego, qr_code;

        public ViewHolderQr(@NonNull View itemView) {
            super(itemView);

            btn_qr = itemView.findViewById(R.id.btn_qr);
            txt_catego = itemView.findViewById(R.id.txt_catego);
            qr_code = itemView.findViewById(R.id.qr_code);

        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_question;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_question = itemView.findViewById(R.id.txt_categories);
        }
    }
}
