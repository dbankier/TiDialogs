package yy.tidialogs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiUIHelper;
import org.appcelerator.titanium.view.TiUIView;
import org.appcelerator.kroll.common.Log;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

@Kroll.proxy(creatableInModule = TidialogsModule.class)
public class MultiPickerProxy extends BaseDialogProxy
{
	private static final String LCAT = "TiDialogs";
	private KrollFunction onChange;

	private class MultiPicker extends BaseUIDialog
	{
		Builder builder;

		public MultiPicker(TiViewProxy proxy)
		{
			super(proxy);
		}

		@Override
		protected AlertDialog getDialog()
		{
			if (dialog != null) {
				return dialog;
			}
			dialog = getBuilder().create();
			dialog.setOnDismissListener(dismissListener);
			builder = null;
			return dialog;
		}

		private Builder getBuilder()
		{
			if (builder == null) {
				builder = new AlertDialog.Builder(this.proxy.getActivity());
				builder.setCancelable(true);
			}
			return builder;
		}

		@Override
		public void processProperties(KrollDict properties)
		{
			super.processProperties(properties);
			String okButtonTitle;
			String cancelButtonTitle;
			String message;
			boolean cancellable = true;
			if (properties.containsKeyAndNotNull("onchange")) {
				Object o = properties.get("onchange");
				if (o instanceof KrollFunction) {
					onChange = (KrollFunction) o;
				}
			}
			if (properties.containsKeyAndNotNull(TiC.PROPERTY_TITLE)) {
				getBuilder().setTitle(properties.getString(TiC.PROPERTY_TITLE));
			}
			if (properties.containsKeyAndNotNull("message")) {
				getBuilder().setMessage(properties.getString("message"));
			}
			if (properties.containsKeyAndNotNull("icon")) {
				Drawable icon = TiUIHelper.getResourceDrawable(resolveUrl(null, properties.getString("icon")));
				getBuilder().setIcon(icon);
			}
			if (properties.containsKeyAndNotNull(TiC.PROPERTY_ANDROID_VIEW)) {
				Object o = properties.get(TiC.PROPERTY_ANDROID_VIEW);
				if (o instanceof TiUIView) {
					TiUIView view = (TiUIView) o;
					getBuilder().setCustomTitle(view.getNativeView());
				}
			}
			if (properties.containsKey("okButtonTitle")) {
				okButtonTitle = properties.getString("okButtonTitle");
			} else {
				okButtonTitle = this.proxy.getActivity().getApplication().getResources().getString(R.string.ok);
			}

			if (properties.containsKeyAndNotNull("cancelButtonTitle")) {
				cancelButtonTitle = properties.getString("cancelButtonTitle");
			} else {
				cancelButtonTitle = this.proxy.getActivity().getApplication().getResources().getString(R.string.cancel);
			}

			if (properties.containsKeyAndNotNull("canCancel")) {
				cancellable = properties.getBoolean("canCancel");
			}

			if (properties.containsKeyAndNotNull("options")) {
				final String[] options = properties.getStringArray("options");

				// only selected items are stored with corresponding index
				final ArrayList<Integer> selectedItems = new ArrayList<Integer>();

				final boolean[] resultList = new boolean[options.length];
				Arrays.fill(resultList, Boolean.FALSE);

				// mark all items as unselected
				boolean[] checked = new boolean[options.length];
				Arrays.fill(checked, Boolean.FALSE);

				// are there any preselections?
				if (properties.containsKeyAndNotNull("selected")) {
					List<String> s = Arrays.asList(properties.getStringArray("selected"));
					for (int i = 0; i < options.length; i++) {
						checked[i] = s.contains(options[i]);
						if (checked[i] == true) {
							resultList[i] = Boolean.TRUE;
							selectedItems.add(i); // keep info about
												  // preselected items!
						}
					}
				}
				getBuilder()
					.setMultiChoiceItems(options, checked,
										 new DialogInterface.OnMultiChoiceClickListener() {
											 // called whenever an item is clicked, toggles
											 // selection info
											 @Override
											 public void onClick(DialogInterface dialog, int which, boolean isChecked)
											 {
												 resultList[which] = isChecked;
												 Log.d(LCAT, resultList.toString());
												 KrollDict kd = new KrollDict();
												 kd.put("index", which);
												 kd.put("checked", isChecked);
												 kd.put("value", isChecked);
												 if (hasListeners("change")) {
													 fireEvent("change", kd);
												 }
												 if (onChange != null)
													 onChange.call(getKrollObject(), kd);
												 if (isChecked) {
													 // we can be sure, item is not already in
													 // selection list
													 selectedItems.add(which);

												 } else if (selectedItems.contains(which)) {
													 selectedItems.remove(Integer.valueOf(which));
												 }
											 }
										 })

					// ok returns indexes of selected items and the corresponding
					// selected items -> wording is not the best
					.setPositiveButton(okButtonTitle, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id)
						{
							// convert to int array

							ArrayList<String> selections = new ArrayList<String>();
							for (Integer s : selectedItems) {
								selections.add(options[s]);
							}

							KrollDict data = new KrollDict();
							data.put("indexes", selectedItems.toArray(new Integer[selectedItems.size()]));
							data.put("selections", selections.toArray(new String[selections.size()]));
							data.put("result", resultList);
							if (hasListeners("click"))
								fireEvent("click", data);
						}
					});

				getBuilder().setCancelable(cancellable);
			}
		}
	}

	public MultiPickerProxy()
	{
		super();
	}

	@Override
	public TiUIView createView(Activity activity)
	{
		return new MultiPicker(this);
	}

	@Override
	public void handleCreationDict(KrollDict options)
	{
		super.handleCreationDict(options);
	}
}
