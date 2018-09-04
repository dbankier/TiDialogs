package yy.tidialogs;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiUIHelper;

@Kroll.proxy
public abstract class BaseDialogProxy extends TiViewProxy
{

	public BaseDialogProxy()
	{
		super();
	}

	@Override
	protected void handleShow(KrollDict options)
	{
		super.handleShow(options);
		// If there's a lock on the UI message queue, there's a good chance
		// we're in the middle of activity stack transitions. An alert
		// dialog should occur above the "topmost" activity, so if activity
		// stack transitions are occurring, try to give them a chance to
		// "settle"
		// before determining which Activity should be the context for the
		// AlertDialog.
		TiUIHelper.runUiDelayedIfBlock(new Runnable() {
			@Override
			public void run()
			{
				getOrCreateView().show();
			}
		});
	}
}
