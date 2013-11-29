package yy.tidialogs;

import java.util.ArrayList;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiUIHelper;
import org.appcelerator.titanium.view.TiUIView;

//import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;


@Kroll.proxy(creatableInModule = TidialogsModule.class)
public class MultiPickerProxy extends TiViewProxy {
	private class MultiPicker extends TiUIView {

		Builder builder;

		public MultiPicker(TiViewProxy proxy) {
			super(proxy);

		}
		
		private Builder getBuilder() {
			if (builder == null) {
			   builder = new AlertDialog.Builder(this.proxy.getActivity());
			   builder.setCancelable(true);
			}
			return builder;
		}

		@Override
		public void processProperties(KrollDict d) {
			super.processProperties(d);
			if (d.containsKey("title")) {
				getBuilder().setTitle(d.getString("title"));
			} else if (d.containsKey("options")) {
				String[] options = d.getStringArray("options"); 
				final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();
				getBuilder().setMultiChoiceItems(options, null, new DialogInterface.OnMultiChoiceClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int which,
		                       boolean isChecked) {
		                   if (isChecked) {
		                       mSelectedItems.add(which);
		                   } else if (mSelectedItems.contains(which)) {
		                       mSelectedItems.remove(Integer.valueOf(which));
		                   }
		               }
		           })
		           .setPositiveButton(/*R.string.ok*/"OK", new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		            	   KrollDict data = new KrollDict();
							data.put("seleted", mSelectedItems);
							fireEvent("click", data);
		               }
		           })
		           .setNegativeButton(/*R.string.cancel*/"Cancel", new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		                   
		               }
		           });
			}
			
		}

		public void show() {
			getBuilder().show();
		}

	}

	public MultiPickerProxy() {
		super();
	}

	@Override
	public TiUIView createView(Activity activity) {
		return new MultiPicker(this);
	}

	@Override
	public void handleCreationDict(KrollDict options) {
		super.handleCreationDict(options);
	}

	@Override
	protected void handleShow(KrollDict options) {
		super.handleShow(options);
		TiUIHelper.runUiDelayedIfBlock(new Runnable() {
			@Override
			public void run() {
				MultiPicker d = (MultiPicker) getOrCreateView();
				d.show();
			}
		});
	}
}