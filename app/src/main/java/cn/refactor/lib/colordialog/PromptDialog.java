package cn.refactor.lib.colordialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import abel.project.twa.vendedor.R;
import cn.refactor.lib.colordialog.util.DisplayUtil;

public class PromptDialog extends Dialog {
    private static final Config BITMAP_CONFIG = Config.ARGB_8888;
    private static final int DEFAULT_RADIUS = 6;
    public static final int DIALOG_TYPE_DEFAULT = 0;
    public static final int DIALOG_TYPE_HELP = 1;
    public static final int DIALOG_TYPE_INFO = 0;
    public static final int DIALOG_TYPE_SUCCESS = 3;
    public static final int DIALOG_TYPE_WARNING = 4;
    public static final int DIALOG_TYPE_WRONG = 2;
    private AnimationSet mAnimIn;
    private AnimationSet mAnimOut;
    private CharSequence mBtnText,mNegative;
    private CharSequence mContent;
    private TextView mContentTv;
    private int mDialogType;
    private View mDialogView;
    private boolean mIsShowAnim;
    private OnNegativeListener mOnNegativeListener;
    private OnPositiveListener mOnPositiveListener;
    private TextView mPositiveBtn;
    private TextView mNegativeBtn;
    private CharSequence mTitle;
    private TextView mTitleTv;

    public interface OnNegativeListener {
        void onClick(PromptDialog promptDialog);
    }

    public interface OnPositiveListener {
        void onClick(PromptDialog promptDialog);
    }

    public PromptDialog(Context context) {
        this(context, 0);
    }

    public PromptDialog(Context context, int theme) {
        super(context, R.style.color_dialog);
        init();
    }

    private void init() {
        this.mAnimIn = AnimationLoader.getInAnimation(getContext());
        this.mAnimOut = AnimationLoader.getOutAnimation(getContext());
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        View contentView = View.inflate(getContext(), R.layout.layout_conexion, null);
        setContentView(contentView);
        resizeDialog();
        this.mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        this.mTitleTv = (TextView) contentView.findViewById(R.id.tvTitle);
        this.mContentTv = (TextView) contentView.findViewById(R.id.tvContent);
        this.mPositiveBtn = (TextView) contentView.findViewById(R.id.btnPositive);
        this.mNegativeBtn = (TextView) contentView.findViewById(R.id.btnNegative);
        View llBtnGroup = findViewById(R.id.llBtnGroup);
        ((ImageView) contentView.findViewById(R.id.logoIv)).setBackgroundResource(getLogoResId(this.mDialogType));
        LinearLayout topLayout = (LinearLayout) contentView.findViewById(R.id.topLayout);
        ImageView triangleIv = new ImageView(getContext());
        triangleIv.setLayoutParams(new LayoutParams(-1, DisplayUtil.dp2px(getContext(), 10.0f)));
        triangleIv.setImageBitmap(createTriangel((int) (((double) DisplayUtil.getScreenSize(getContext()).x) * 0.7d), DisplayUtil.dp2px(getContext(), 10.0f)));
        topLayout.addView(triangleIv);
        setBtnBackground(this.mPositiveBtn);
        //setBtnBackground(this.mNegativeBtn);
        setBottomCorners(llBtnGroup);
        int radius = DisplayUtil.dp2px(getContext(), 6.0f);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{(float) radius, (float) radius, (float) radius, (float) radius, 0.0f, 0.0f, 0.0f, 0.0f}, null, null));
        shapeDrawable.getPaint().setStyle(Style.FILL);
        shapeDrawable.getPaint().setColor(getContext().getResources().getColor(getColorResId(this.mDialogType)));
        ((LinearLayout) findViewById(R.id.llTop)).setBackgroundDrawable(shapeDrawable);
        this.mTitleTv.setText(this.mTitle);
        this.mContentTv.setText(this.mContent);
        this.mPositiveBtn.setText(this.mBtnText);
        //this.mNegativeBtn.setText(this.mNegative);
    }

    private void resizeDialog() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (((double) DisplayUtil.getScreenSize(getContext()).x) * 0.7d);
        getWindow().setAttributes(params);
    }

    protected void onStart() {
        super.onStart();
        startWithAnimation(this.mIsShowAnim);
    }

    public void dismiss() {
        dismissWithAnimation(this.mIsShowAnim);
    }

    private void startWithAnimation(boolean showInAnimation) {
        if (showInAnimation) {
            this.mDialogView.startAnimation(this.mAnimIn);
        }
    }

    private void dismissWithAnimation(boolean showOutAnimation) {
        if (showOutAnimation) {
            this.mDialogView.startAnimation(this.mAnimOut);
        } else {
            super.dismiss();
        }
    }

    private int getLogoResId(int mDialogType) {
        if (mDialogType == 0) {
            return R.drawable.ic_info;
        }
        if (mDialogType == 0) {
            return R.drawable.ic_info;
        }
        if (1 == mDialogType) {
            return R.drawable.ic_info;
        }
        if (2 == mDialogType) {
            return R.drawable.ic_wrong;
        }
        if (3 == mDialogType) {
            return R.drawable.ic_success;
        }
        if (4 == mDialogType) {
            return R.drawable.icon_warning;
        }
        return R.drawable.ic_info;
    }

    private int getColorResId(int mDialogType) {
        if (mDialogType == 0) {
            return R.color.color_type_info;
        }
        if (mDialogType == 0) {
            return R.color.color_type_info;
        }
        if (1 == mDialogType) {
            return R.color.color_type_help;
        }
        if (2 == mDialogType) {
            return R.color.color_type_wrong;
        }
        if (3 == mDialogType) {
            return R.color.color_type_success;
        }
        if (4 == mDialogType) {
            return R.color.color_type_warning;
        }
        return R.color.color_type_info;
    }

    private int getSelBtn(int mDialogType) {
        if (mDialogType == 0) {
            return R.drawable.sel_btn;
        }
        if (mDialogType == 0) {
            return R.drawable.sel_btn_info;
        }
        if (1 == mDialogType) {
            return R.drawable.sel_btn_help;
        }
        if (2 == mDialogType) {
            return R.drawable.sel_btn_wrong;
        }
        if (3 == mDialogType) {
            return R.drawable.sel_btn_success;
        }
        if (4 == mDialogType) {
            return R.drawable.sel_btn_warning;
        }
        return R.drawable.sel_btn;
    }

    private void initAnimListener() {
        this.mAnimOut.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                PromptDialog.this.mDialogView.post(new Runnable() {
                    public void run() {
                        PromptDialog.this.callDismiss();
                    }
                });
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void initListener() {
        this.mPositiveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (PromptDialog.this.mOnPositiveListener != null) {
                    PromptDialog.this.mOnPositiveListener.onClick(PromptDialog.this);
                }
            }
        });
        /*this.mNegativeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (PromptDialog.this.mOnNegativeListener != null) {
                    PromptDialog.this.mOnNegativeListener.onClick(PromptDialog.this);
                }
            }
        });*/
        initAnimListener();
    }

    private void callDismiss() {
        super.dismiss();
    }

    private Bitmap createTriangel(int width, int height) {
        if (width <= 0 || height <= 0) {
            return null;
        }
        return getBitmap(width, height, getContext().getResources().getColor(getColorResId(this.mDialogType)));
    }

    private Bitmap getBitmap(int width, int height, int backgroundColor) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(1);
        paint.setColor(backgroundColor);
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.lineTo((float) width, 0.0f);
        path.lineTo((float) (width / 2), (float) height);
        path.close();
        canvas.drawPath(path, paint);
        return bitmap;
    }

    private void setBtnBackground(TextView btnOk) {
        btnOk.setTextColor(createColorStateList(getContext().getResources().getColor(R.color.white),
                getContext().getResources().getColor(R.color.color_dialog_gray)));
        btnOk.setBackgroundDrawable(getContext().getResources().getDrawable(getSelBtn(mDialogType)));
        if (btnOk.getId() == R.id.btnNegative && this.mOnNegativeListener != null) {
            btnOk.setVisibility(View.VISIBLE);
        }
    }

    private void setBottomCorners(View llBtnGroup) {
        int radius = DisplayUtil.dp2px(getContext(), 6.0f);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{0.0f, 0.0f, 0.0f, 0.0f, (float) radius, (float) radius, (float) radius, (float) radius}, null, null));
        shapeDrawable.getPaint().setColor(-1);
        shapeDrawable.getPaint().setStyle(Style.FILL);
        llBtnGroup.setBackgroundDrawable(shapeDrawable);
    }

    private ColorStateList createColorStateList(int normal, int pressed) {
        return createColorStateList(normal, pressed, ViewCompat.MEASURED_STATE_MASK, ViewCompat.MEASURED_STATE_MASK);
    }

    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{16842919, 16842910};
        states[1] = new int[]{16842910, 16842908};
        states[2] = new int[]{16842910};
        states[3] = new int[]{16842908};
        states[4] = new int[]{16842909};
        states[5] = new int[0];
        return new ColorStateList(states, colors);
    }

    public PromptDialog setAnimationEnable(boolean enable) {
        this.mIsShowAnim = enable;
        return this;
    }

    public PromptDialog setTitleText(CharSequence title) {
        this.mTitle = title;
        return this;
    }

    public PromptDialog setTitleText(int resId) {
        return setTitleText(getContext().getString(resId));
    }

    public PromptDialog setContentText(CharSequence content) {
        this.mContent = content;
        return this;
    }

    public PromptDialog setContentText(int resId) {
        return setContentText(getContext().getString(resId));
    }

    public TextView getTitleTextView() {
        return this.mTitleTv;
    }

    public TextView getContentTextView() {
        return this.mContentTv;
    }

    public int getDialogType() {
        return this.mDialogType;
    }

    public PromptDialog setDialogType(int type) {
        this.mDialogType = type;
        return this;
    }

    public PromptDialog setPositiveListener(CharSequence btnText, OnPositiveListener l) {
        this.mBtnText = btnText;
        return setPositiveListener(l);
    }

    public PromptDialog setNegativeListener(CharSequence btnText, OnNegativeListener l) {
        this.mNegative = btnText;
        return setNegativeListener(l);
    }

    public PromptDialog setPositiveListener(int stringResId, OnPositiveListener l) {
        return setPositiveListener(getContext().getString(stringResId), l);
    }

    public PromptDialog setNegativeListener(int stringResId, OnNegativeListener l) {
        return setNegativeListener(getContext().getString(stringResId), l);
    }

    public PromptDialog setPositiveListener(OnPositiveListener l) {
        this.mOnPositiveListener = l;
        return this;
    }

    public PromptDialog setNegativeListener(OnNegativeListener l) {
        this.mOnNegativeListener = l;
        return this;
    }

    public PromptDialog setAnimationIn(AnimationSet animIn) {
        this.mAnimIn = animIn;
        return this;
    }

    public PromptDialog setAnimationOut(AnimationSet animOut) {
        this.mAnimOut = animOut;
        initAnimListener();
        return this;
    }
}
