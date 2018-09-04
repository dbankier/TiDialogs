package yy.tidialogs;

import android.app.AlertDialog;

import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

public abstract class BaseUIDialog extends TiUIView
{
	protected AlertDialog dialog;

	public BaseUIDialog(TiViewProxy proxy)
	{
		super(proxy);
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
