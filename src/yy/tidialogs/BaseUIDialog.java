package yy.tidialogs;

import android.app.AlertDialog;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseUIDialog extends TiUIView
{
	protected AlertDialog dialog;

	private AtomicInteger dismissCallCount = new AtomicInteger(0);
	protected OnDismissListener dismissListener;

	public BaseUIDialog(TiViewProxy proxy)
	{
		super(proxy);
		dismissListener = new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog)
			{
				if (dismissCallCount.get() == 0) {
					dismissCallCount.incrementAndGet();
					KrollDict data = new KrollDict();
					data.put("cancel", true);
					data.put("value", null);
					fireEvent("cancel", data);
				}
			}
		};
	}

	abstract protected AlertDialog getDialog();

	@Override
	public void show()
	{
		getDialog().show();
	}

	@Override
	public void hide()
	{
		if (dialog != null) {
			dialog.hide();
		}
	}

	@Override
	public void release()
	{
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
}
