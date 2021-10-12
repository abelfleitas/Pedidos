package abel.project.twa.vendedor.barcodescanner;

import android.content.Intent;
import android.hardware.Camera;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import abel.project.twa.vendedor.ProductoActivity;
import abel.project.twa.vendedor.R;
import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class FullScannerFragment extends Fragment implements
        ZBarScannerView.ResultHandler, FormatSelectorDialogFragment.FormatSelectorDialogListener,
        CameraSelectorDialogFragment.CameraSelectorDialogListener {
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ZBarScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;
    private Bundle bundle;

    boolean flash;
    boolean focus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        mScannerView = new ZBarScannerView(getActivity());
        if(state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
            getCamaraId();
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            getCamaraId();
        }
        setupFormats();

        flash=PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("flash", false);
        focus=PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("autofocus",true);
        String camera=PreferenceManager.getDefaultSharedPreferences(getContext()).getString("camera", "trasera");
        HashSet<String> formatos= (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(getContext()).getStringSet("formatos",new HashSet<String>(BarcodeFormat.ALL_FORMATS.size()));

        List<BarcodeFormat> formats = BarcodeFormat.ALL_FORMATS;
        List<BarcodeFormat> f=new ArrayList();
        if(formatos.size()>0){
            for (int i=0; i<formats.size();i++){
                if(formatos.contains(formats.get(i).getName().toLowerCase())){
                    f.add(formats.get(i));
                }
            }
        }
        mScannerView.setFormats(f);
        mScannerView.setFlash(flash);
        mScannerView.setAutoFocus(focus);
        return mScannerView;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setHasOptionsMenu(true);
        bundle = getActivity().getIntent().getExtras();
    }

    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem;

        if(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("flash", false)) {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_on);
            menuItem.setIcon(R.drawable.ic_flash_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_off);
            menuItem.setIcon(R.drawable.ic_flash_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("flash", false)) {
                    item.setIcon(R.drawable.ic_flash_off);
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("flash", false).apply();
                    mScannerView.setFlash(false);
                } else {
                    item.setIcon(R.drawable.ic_flash_on);
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("flash", true).apply();
                    mScannerView.setFlash(true);
                }
                return false;
            }
        });

        menuItem = menu.add(Menu.NONE, R.id.menu_camera_selector, 0, R.string.select_camera);
        menuItem.setIcon(R.drawable.ic_camera);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_camera_selector:
                mScannerView.stopCamera();
                DialogFragment cFragment = CameraSelectorDialogFragment.newInstance(this, mCameraId);
                cFragment.show(getActivity().getSupportFragmentManager(), "camera_selector");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(flash);
        mScannerView.setAutoFocus(focus);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {}
        Intent intent = new Intent(getActivity(),ProductoActivity.class);
        intent.putExtra("codigo_barra",rawResult.getContents());
        intent.putExtra("cliente",bundle.getString("cliente"));
        intent.putExtra("convenio",bundle.getString("convenio"));
        intent.putExtra("producto",bundle.getString("producto"));
        intent.putExtra("cantidad",bundle.getDouble("cantidad"));

        intent.putExtra("objBodega",(Serializable) bundle.get("objBodega"));
        intent.putExtra("objCliente",(Serializable) bundle.get("objCliente"));
        intent.putExtra("objConvenio",(Serializable) bundle.get("objConvenio"));
        intent.putExtra("objProducto", (Serializable) bundle.get("objProducto"));

        intent.putExtra("codConv",bundle.getString("codConv"));
        intent.putExtra("id",bundle.getInt("id"));

        startActivity(intent);
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .commit();
        getActivity(). overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
        getActivity().finish();
    }


    @Override
    public void onFormatsSaved(ArrayList<Integer> selectedIndices) {
        mSelectedIndices = selectedIndices;
        setupFormats();
    }

    @Override
    public void onCameraSelected(int cameraId) {
        mCameraId = cameraId;
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(flash);
        mScannerView.setAutoFocus(focus);
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
        if(mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("camera","frontal").apply();
        } else if(mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("camera","trasera").apply();
        } else {
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("camera","trasera").apply();
        }
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if(mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for(int i = 0; i < BarcodeFormat.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for(int index : mSelectedIndices) {
            formats.add(BarcodeFormat.ALL_FORMATS.get(index));
        }               // Fin de ciclo
        if(mScannerView != null) {
            mScannerView.setFormats(formats);
        }               // Fin de condicional
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    public void getCamaraId(){
        if(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("camera","trasera")==null
                  || PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("camera","trasera").isEmpty()){
            mCameraId=Camera.CameraInfo.CAMERA_FACING_BACK;
        }else{
            if(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("camera","trasera").equalsIgnoreCase("frontal")){
                mCameraId=Camera.CameraInfo.CAMERA_FACING_FRONT;
            }else{
                mCameraId=Camera.CameraInfo.CAMERA_FACING_BACK;
            }
        }
    }
}
