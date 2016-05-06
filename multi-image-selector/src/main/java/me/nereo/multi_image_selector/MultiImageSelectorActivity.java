package me.nereo.multi_image_selector;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 多图选择
 * Created by Nereo on 2015/4/7.
 * Updated by nereo on 2016/1/19.
 */
public class MultiImageSelectorActivity extends FragmentActivity implements MultiImageSelectorFragment.Callback{

    /**
     * 是否显示隐藏目录下的文件或者隐藏文件，默认不显示
     */
    public static final String EXTRA_SHOW_HIDDEN_FILES = "show_hidden_files";
    /** 最大图片选择次数，int类型，默认9 */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /** 图片选择模式，默认多选 */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /** 是否显示相机，默认显示 */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合  */
    public static final String EXTRA_RESULT = "select_result";
    /** 默认选择集 */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    /** 单选 */
    public static final int MODE_SINGLE = 0;
    /** 多选 */
    public static final int MODE_MULTI = 1;

    private ArrayList<String> resultList = new ArrayList<>();
    private Button mSubmitButton;
    private int mDefaultCount;

    /**
     * @param applicationContext
     * @param alreadySelected
     * @param showHidden 是否显示隐藏目录下的文件或者隐藏文件     默认不显示
     * @return
     */
    public static Intent makeIntentForNineChoose(Context applicationContext, List<String> alreadySelected, boolean showHidden) {
        Intent intent = new Intent(applicationContext, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        intent.putExtra(EXTRA_SHOW_HIDDEN_FILES, showHidden);
        // 默认选择
        if(alreadySelected != null && alreadySelected.size()>0){
            ArrayList<String> selected = new ArrayList<String>(alreadySelected);
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, selected);
        }
        return intent;
    }

    public static Intent makeIntentForNineChoose(Context applicationContext, List<String> alreadySelected) {
        return makeIntentForNineChoose(applicationContext, alreadySelected, false);
    }


    public static Intent makeIntentForSingleChoose(Context applicationContext, final String alreadySelected, boolean showHidden) {
        Intent intent = new Intent(applicationContext, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        intent.putExtra(EXTRA_SHOW_HIDDEN_FILES, showHidden);
        // 默认选择
        if(!TextUtils.isEmpty(alreadySelected)){
            ArrayList<String> selected = new ArrayList<String>(){{
                add(alreadySelected);
            }};
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, selected);
        }
        return intent;
    }

    public static Intent makeIntentForSingleChoose(Context applicationContext, final String alreadySelected) {
        return makeIntentForSingleChoose(applicationContext, alreadySelected, false);
    }

    public static Intent makeIntentForMultiChoose(Context applicationContext, int maxImageCount, List<String> alreadySelected, boolean showHidden) {
        Intent intent = new Intent(applicationContext, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxImageCount);
        intent.putExtra(EXTRA_SHOW_HIDDEN_FILES, showHidden);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择
        if(alreadySelected != null && alreadySelected.size()>0){
            ArrayList<String> selected = new ArrayList<String>(alreadySelected);
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, selected);
        }
        return intent;
    }

    public static Intent makeIntentForMultiChoose(Context applicationContext, int maxImageCount, List<String> alreadySelected) {
        return makeIntentForMultiChoose(applicationContext, maxImageCount, alreadySelected, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
        int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        if(mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }

        boolean showHidden = intent.getBooleanExtra(EXTRA_SHOW_HIDDEN_FILES, false);
        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_HIDDEN_FILES, showHidden);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                .commit();

        // 返回按钮
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // 完成按钮
        mSubmitButton = (Button) findViewById(R.id.commit);
        if(resultList == null || resultList.size()<=0){
            mSubmitButton.setText(getBtnText(0, mDefaultCount));
            mSubmitButton.setEnabled(false);
        }else{
            mSubmitButton.setText(getBtnText(resultList.size(), mDefaultCount));
            mSubmitButton.setEnabled(true);
        }
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultList != null && resultList.size() >0){
                    // 返回已选择的图片数据
                    Intent data = new Intent();
                    data.putStringArrayListExtra(EXTRA_RESULT, resultList);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
    }

    private void updateDoneText(){
        mSubmitButton.setText(String.format("%s(%d/%d)",
                getString(R.string.action_done), resultList.size(), mDefaultCount));
    }

    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path) {
        if(!resultList.contains(path)) {
            resultList.add(path);
        }
        // 有图片之后，改变按钮状态
        if(resultList.size() > 0){
            mSubmitButton.setText(getBtnText(resultList.size(), mDefaultCount));
            if(!mSubmitButton.isEnabled()){
                mSubmitButton.setEnabled(true);
            }
        }
    }

    @Override
    public void onImageUnselected(String path) {
        if(resultList.contains(path)){
            resultList.remove(path);
            mSubmitButton.setText(getBtnText(resultList.size(), mDefaultCount));
        }else{
            mSubmitButton.setText(getBtnText(resultList.size(), mDefaultCount));
        }
        updateDoneText();
        // 当为选择图片时候的状态
        if(resultList.size() == 0){
            mSubmitButton.setText(getBtnText(resultList.size(), mDefaultCount));
            mSubmitButton.setEnabled(false);
        }
    }

    @Override
    public void onCameraShot(File imageFile) {
        if(imageFile != null) {

            // notify system
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));

            Intent data = new Intent();
            resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }


    protected String getBtnText(int resultSize, int maxCount){
        if(resultSize <=0){
            return "完成";
        } else {
            return String.format("完成(%d/%d)", resultSize, maxCount);
        }
    }
}
