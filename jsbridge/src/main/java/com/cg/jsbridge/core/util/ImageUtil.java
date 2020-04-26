package com.cg.jsbridge.core.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.ExifInterface;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.graphics.Canvas.ALL_SAVE_FLAG;

public class ImageUtil {

	private static ImageUtil imageUtil;

	public static ImageUtil getInstance() {
		if (imageUtil == null) {
			imageUtil = new ImageUtil();
			return imageUtil;
		}
		return imageUtil;
	}

	/**
	 * @brief 通过画笔从新绘制图片
	 * @param d
	 *            图片对象
	 * @param p
	 *            画笔
	 * @return Drawable
	 * */
	private Drawable createDrawable(Drawable d, Paint p) {
		BitmapDrawable bd = (BitmapDrawable) d;
		Bitmap b = bd.getBitmap();
		Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(),
				bd.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b, 0, 0, p); // 关键代码，使用新的Paint画原图，
		return new BitmapDrawable(bitmap);
	}

	/**
	 * @brief 设置Selector。 本次只增加点击变暗的效果，注释的代码为更多的效果
	 * @param drawable
	 *            图片对象
	 * @return StateListDrawable
	 * */
	private StateListDrawable createSLD(Drawable drawable) {
		StateListDrawable bg = new StateListDrawable();
		Paint p = new Paint();
		p.setColor(0x40222222); // Paint ARGB色值，A = 0x40 不透明。RGB222222 暗色

		Drawable normal = drawable;
		Drawable pressed = createDrawable(drawable, p);
		// p = new Paint();
		// p.setColor(0x8000FF00);
		// Drawable focused = createDrawable(drawable, p);
		// p = new Paint();
		// p.setColor(0x800000FF);
		// Drawable unable = createDrawable(drawable, p);
		// View.PRESSED_ENABLED_STATE_SET
		bg.addState(new int[] { android.R.attr.state_pressed,
				android.R.attr.state_enabled }, pressed);
		// View.ENABLED_FOCUSED_STATE_SET
		// bg.addState(new int[] { android.R.attr.state_enabled,
		// android.R.attr.state_focused }, focused);
		// View.ENABLED_STATE_SET
		bg.addState(new int[] { android.R.attr.state_enabled }, normal);
		// View.FOCUSED_STATE_SET
		// bg.addState(new int[] { android.R.attr.state_focused }, focused);
		// // View.WINDOW_FOCUSED_STATE_SET
		// bg.addState(new int[] { android.R.attr.state_window_focused },
		// unable);
		// View.EMPTY_STATE_SET
		bg.addState(new int[] {}, normal);
		return bg;
	}
	
	/**
	 * @brief 获取带点击效果的图片对象
	 * @param drawable
	 *            图片对象
	 * @return StateListDrawable
	 * */
	public StateListDrawable getStateListDrawable(Drawable drawable) {
		return createSLD(drawable);
	}
	

	/**
	 * @brief 从新绘制drawable尺寸
	 * @param drawable
	 *            图片对象
	 * @param w
	 *            图片修改后宽度
	 * @param h
	 *            图片修改后高度
	 * @return Drawable
	 * */
	@SuppressWarnings("deprecation")
	public Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldMp = FormatTools.getInstance().Drawable2Bitmap(drawable);
		Matrix matrix = new Matrix();
		if (w==0) {
			float scaleHeight = (float) h / height;
			matrix.postScale(scaleHeight, scaleHeight);
		}else if (h==0) {
			float scaleWidth = (float) w / width;
			matrix.postScale(scaleWidth, scaleWidth);
		}else {
			float scaleWidth = (float) w / width;
			float scaleHeight = (float) h / height;
			matrix.postScale(scaleWidth, scaleHeight);
		}
		Bitmap newbmp = Bitmap.createBitmap(oldMp, 0, 0, width, height, matrix,
				true);
		return new BitmapDrawable(newbmp);
	}
	/**
	 * @brief bitmap转drawable 转换时drawable像素会缩小一半 所以用下面的方法
	 * @param bitmap
	 *            图片对象（bitmap）
	 * @param activity
	 *            activity对象
	 * @return Drawable
	 * */
	public Drawable getDrawable(Bitmap bitmap, Activity activity) {
		ImageView aImageView = new ImageView(activity);
		aImageView.setImageBitmap(bitmap);
		return aImageView.getDrawable();
	}

	/**
	 * @brief 代码动态设置RadioButton的背景图片 用来做换肤
	 * @param normal
	 *            原始图片对象（Drawable）
	 * @return StateListDrawable
	 * */
	public StateListDrawable getRadioButtonBg(Drawable normal) {
		Paint p = new Paint();
		p.setColor(0x40222222); // Paint ARGB色值，A = 0x40 不透明。RGB222222 暗色
		Drawable pressed = createDrawable(normal, p);
		return newSelector(normal, pressed);
	}

	/**
	 * @brief 代码动态设置RadioButton的背景图片 用来做换肤
	 * @param normal
	 *            正常状态下图片显示
	 * @param pressed
	 *            选中状态图片显示
	 * @return StateListDrawable
	 * */
	public StateListDrawable getRadioButtonBg(Drawable normal, Drawable pressed) {
		return newSelector(normal, pressed);
	}

	/**
	 * @brief 设置Selector
	 * @param normal
	 *            初始状态图片
	 * @param pressed
	 *            状态改变时图片
	 * @return StateListDrawable
	 * */
	private StateListDrawable newSelector(Drawable normal, Drawable pressed) {
		StateListDrawable bg = new StateListDrawable();
		// View.state_pressed
		bg.addState(new int[] { android.R.attr.state_pressed }, pressed);
		// View.FOCUSED_STATE_SET
		bg.addState(new int[] { android.R.attr.state_focused }, pressed);
		// View.state_checked
		bg.addState(new int[] { android.R.attr.state_checked }, pressed);
		// View.EMPTY_STATE_SET
		bg.addState(new int[] {}, normal);
		return bg;
	}

	/**
	 * @brief 获取view截图
	 * @param view
	 *            需要截图的view
	 * @return Drawable
	 * */
	public Drawable getViewScreenShot(View view) {
		Bitmap obmp = null;
		BitmapDrawable transitionsDrawable = null;
		view.setDrawingCacheEnabled(true);// 设置图片缓冲区
		if (view.getDrawingCache() != null) {
			obmp = Bitmap.createBitmap(view.getDrawingCache());
			view.setDrawingCacheEnabled(false);// 清空图片缓冲区
			transitionsDrawable = new BitmapDrawable(obmp);
			obmp.recycle();
		}
		return transitionsDrawable;
	}
	/**
	 * 获取透明图片
	 * */
	public Drawable getScaleDrawable(Drawable drawable, Activity activity){
		Paint p = new Paint();
		p.setColor(0x40222222); // Paint ARGB色值，A = 0x40 不透明。RGB222222 暗色
		BitmapDrawable bd = (BitmapDrawable) drawable;
		Bitmap b = bd.getBitmap();
		Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(),
				bd.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b, 0, 0, p); // 关键代码，使用新的Paint画原图，
		return getDrawable(bitmap, activity);
	}

    /**
	 * Take Screen Shot
	 *
	 * @param activity
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap takeScreenShot(Activity activity) {
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();


		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;


		// 获取屏幕长和高
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();

		int targetHeight = height - statusBarHeight;
		if (targetHeight > b1.getHeight()) {
			targetHeight = b1.getHeight() - statusBarHeight;
		}

		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width,
				targetHeight);
		view.destroyDrawingCache();
		return b;
	}


	  public static Bitmap drawableToBitamp(Drawable drawable)
	      {
	          int w = drawable.getIntrinsicWidth();
	          int h = drawable.getIntrinsicHeight();
	          System.out.println("Drawable转Bitmap");
	          Bitmap.Config config =
	                  drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
	                          : Bitmap.Config.RGB_565;
	         Bitmap bitmap = Bitmap.createBitmap(w,h,config);
	        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
	         Canvas canvas = new Canvas(bitmap);
	         drawable.setBounds(0, 0, w, h);
	         drawable.draw(canvas);
	         return bitmap;
	  }


	  /**
	     * 旋转图片
	     */
	    public static Bitmap rotateBitmap(Context context, Bitmap bitmap, String filepath) {
	        int degrees = getExifOrientation(filepath) ;
//	        bitmap=getBitMap(filepath,1080,1920);
	        bitmap=zoomImage(bitmap, (float)0.5);
	        if (degrees != 0 && bitmap != null) {
	            Matrix m = new Matrix();
	            m.postRotate(degrees);
//	            (degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
	            try {
	            	 Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
	            			 bitmap.getWidth(),bitmap.getHeight(), m, true);


	            	 resizedBitmap.getByteCount();
	            	 bitmap.recycle();
	            	 return resizedBitmap;
	            } catch (OutOfMemoryError ex) {
	            	System.out.println(ex.toString());
	            }
	        }
	        return bitmap;
	    }


	    /**
	     * 获取图片旋转角度
	     */
	    //判断图片的旋转角度
	    public static int getExifOrientation(String filepath) {
	        int degree = 0;
	        ExifInterface exif = null;
	        try {
	            exif = new ExifInterface(filepath);
	        } catch (IOException ex) {
	            //Log.e("---->", ex.getMessage());
	        }
	        if (exif != null) {
				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
						-1);

	            if (orientation != -1) {
	                // We only recognize a subset of orientation tag values.
	                switch (orientation) {
	                    case ExifInterface.ORIENTATION_ROTATE_90:
	                        degree = 90;
	                        break;
	                    case ExifInterface.ORIENTATION_ROTATE_180:
	                        degree = 180;
	                        break;
	                    case ExifInterface.ORIENTATION_ROTATE_270:
	                        degree = 270;
	                        break;
	                    default:
	                        break;
	                }
	            }
	        }
	        return degree;
	    }



	   /** 质量压缩不会减少图片的像素，它是在保持像素的前提下改变图片的位深及透明度，来达到压缩图片的目的，图片的长，宽，像素都不会改变，
	    * 那么bitmap所占内存大小是不会变的。
	    * 我们可以看到有个参数：quality，可以调节你压缩的比例，
	    * 但是还要注意一点就是，质量压缩堆png格式这种图片没有作用，因为png是无损压缩。
	    * */
	    public static Bitmap compressQuality(Bitmap bm) {
//	        mSrcSize = bm.getByteCount() + "byte";
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
	        byte[] bytes = bos.toByteArray();
	        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	    }



	    /**
	     * 采样率压缩
	     * 采样率压缩是改变了图片的像素，他是通过先读取图片的边，然后在自己设定图片的边，然后根据设定，读取图片的像素。
	     * 在读取的时候，并不是所有的像素都读取，而是由选择的。所以这种方式减少了像素的个数，能改变图片在内存中的占用大小。
	     * 采样率压缩，的的确确的改变了图片占用内存问题，但是由于像素改变，压缩容易造成失真问题。
	     * 使用采样率压缩，不需要一开始把图片完全读取到内存，而是先读取图片的边，然后设置图片的尺寸，然后再根据尺寸，选择的读取像素。
	     * 这种方法避免了一开始就吧图片读入内存而造成的oom异常。
	     * @param imagePath 图片路径
	     * @param sampleSize 压缩倍率，长宽的缩放比例
	     * @return 将图片的内存压缩为sampleSize的平方倍
	     */
	    public static Bitmap compressImageToBitmap(String imagePath, int sampleSize){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;//读取图片的边界值
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options);
			if(bitmap != null){
				System.out.println("bitmap=========1>>>>"+bitmap.getByteCount());
			}else{
				System.out.println("null");
			}
			options.inJustDecodeBounds = false;
			options.inSampleSize = sampleSize;

			bitmap = BitmapFactory.decodeFile(imagePath,options);
			System.out.println("bitmap========="+sampleSize+">>>>"+bitmap.getByteCount());
			return bitmap;
		}



	    /**
	     * 放缩法压缩
	     * 使用的是通过矩阵对图片进行裁剪，也是通过缩放图片尺寸，来达到压缩图片的效果，和采样率的原理一样。
	     * @param bm
	     * @param sx   长度缩放比例
	     * @param sy   宽度缩放比例
	     * @return
	     */
	    public static Bitmap compressMatrix(Bitmap bm, float sx, float sy) {
	    	Matrix matrix = new Matrix();
	    	matrix.setScale(sx, sy);
	    	try {
	    		return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
	    	} catch (Exception e) {
	    		// TODO: handle exception
	    	}
	    	return bm;
	    }



	    /**
	     * RGB_565压缩
	     * 这是通过压缩像素占用的内存来达到压缩的效果。
	     * 一般不建议使用ARGB_4444，因为画质实在是辣鸡。
	     * 如果对透明度没有要求，建议可以改成RGB_565，相比ARGB_8888将节省一半的内存开销。
	     * @param imagePath
	     * @return
	     */
	    public static Bitmap compressRGB565(String imagePath) {
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inPreferredConfig = Bitmap.Config.RGB_565;
	        try {
	        	 return  BitmapFactory.decodeFile(imagePath, options);
			} catch (Exception e) {
				// TODO: handle exception
			}
	       return null;
	    }

	    /**
	     * 创建指定的图片尺寸
	     * @param bm
	     * @param width
	     * @param height
	     * @return
	     */
	    public static Bitmap compressScaleBitmap(Bitmap bm, int width, int height) {
	    	try {
	    		return  Bitmap.createScaledBitmap(bm, width, height, true);
			} catch (Exception e) {
				// TODO: handle exception
			}
	    	return null;
	    }
	    
	    
	    /**
	     * 获取option
	     * @param imagePath
	     * @return
	     */
	    public static BitmapFactory.Options getBitmapInfo(String imagePath){
	    	BitmapFactory.Options options=new BitmapFactory.Options();
	    	options.inJustDecodeBounds=true;
	    	BitmapFactory.decodeFile(imagePath,options);
	    	return options;
	    }
	    
	    
	    /**
	     * 获取最优的缩放比例
	     * @param ops
	     * @param reWidth
	     * @param reHeight
	     * @return
	     */
	    public static int calculateInSampleSize(BitmapFactory.Options ops, int reWidth, int reHeight){
	    	int imageHeight=ops.outHeight;
	    	int imageWidth=ops.outWidth;
	    	int inSampleSize = 1;
	    	int heightRadio=imageHeight/2;
	    	int widthRadio=imageWidth/2;
	    	while(heightRadio/inSampleSize>reHeight||widthRadio/inSampleSize>reWidth){
	    		inSampleSize*=2;
	    	}
	    	return inSampleSize;
	    }
	    
	    /**
	     * 获取最优的缩放比例
	     * @param ops
	     * @param reWidth
	     * @return
	     */
	    public static int calculateInSampleSize(BitmapFactory.Options ops, int reWidth){
	    	int imageWidth=ops.outWidth;
	    	int inSampleSize = 1;
	    	int widthRadio=imageWidth/2;
	    	while(widthRadio/inSampleSize>reWidth){
	    		inSampleSize*=2;
	    	}
	    	return inSampleSize;
	    }
	    
	    public static Bitmap getBitMap(String imgPath, int reWidth, int reHeight){
	    	BitmapFactory.Options bop=getBitmapInfo(imgPath);
	    	if(reHeight!=0) {
	    		bop.inSampleSize = calculateInSampleSize(bop, reWidth, reHeight);
	    	}else{
	    		bop.inSampleSize = calculateInSampleSize(bop, reWidth);
	    	}
	    	bop.inJustDecodeBounds=false;
	    	Bitmap bitMap= BitmapFactory.decodeFile(imgPath,bop);
	    	return bitMap;
	    }
	    
	    
	    public static Bitmap zoomImage(Bitmap bitmap) {
	        float width = (float)bitmap.getWidth();
	        float height = (float)bitmap.getHeight();
	        float scaleWidth = 0.2F;
	        float scaleHeight = 0.2F;
	        Matrix matrix = new Matrix();
	        matrix.postScale(scaleWidth, scaleHeight);
	        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int)width, (int)height, matrix, true);
	        return newBitmap;
	    }
	    
	    public static Bitmap zoomImage(Bitmap bitmap, float scale) {
	        float width = (float)bitmap.getWidth();
	        float height = (float)bitmap.getHeight();
	        float scaleWidth = scale;
	        float scaleHeight = scale;
	        Matrix matrix = new Matrix();
	        matrix.postScale(scaleWidth, scaleHeight);
	        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int)width, (int)height, matrix, true);
	        return newBitmap;
	    }

	    public static String base64(Bitmap bitmap) {
	        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
	        zoomImage(bitmap).compress(CompressFormat.JPEG, 80, bStream);
	        byte[] bytes = bStream.toByteArray();
	        String result = Base64.encodeToString(bytes, 2);
	        return result;
	    }
	
}
