package yy.tidialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiUIHelper;
import org.appcelerator.titanium.view.TiUIView;
import org.appcelerator.kroll.common.Log;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

@Kroll.proxy(creatableInModule = TidialogsModule.class)
public class MultiPickerProxy extends TiViewProxy {
	private static final String LCAT = TidialogsModule.LCAT;
	private KrollFunction onChange;

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
		public void processProperties(KrollDict properties) {
			super.processProperties(properties);
			String okButtonTitle;
			String cancelButtonTitle;

			boolean cancellable = true;
			if (properties.containsKeyAndNotNull("onchange")) {
				Object o = properties.get("onchange");
				if (o instanceof KrollFunction) {
					onChange = (KrollFunction) o;
				}
			}
			if (properties.containsKey("title")) {
				getBuilder().setTitle(properties.getString("title"));
			}

			if (properties.containsKey("okButtonTitle")) {
				okButtonTitle = properties.getString("okButtonTitle");
			} else {
				okButtonTitle = this.proxy.getActivity().getApplication()
						.getResources().getString(R.string.ok);
			}

			if (properties.containsKey("cancelButtonTitle")) {
				cancelButtonTitle = properties.getString("cancelButtonTitle");
			} else {
				cancelButtonTitle = this.proxy.getActivity().getApplication()
						.getResources().getString(R.string.cancel);
			}

			if (properties.containsKey("canCancel")) {
				cancellable = properties.getBoolean("canCancel");
			}

			if (properties.containsKey("options")) {
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
					List<String> s = Arrays.asList(properties
							.getStringArray("selected"));
					for (int i = 0; i < options.length; i++) {
						checked[i] = s.contains(options[i]);
						if (checked[i] == true) {
							resultList[i] = Boolean.TRUE;
							selectedItems.add(i); // keep info about
													// preselected items!
						}
					}
				}
				getBuilder().setMultiChoiceItems(options, checked,
						new DialogInterface.OnMultiChoiceClickListener() {
							// called whenever an item is clicked, toggles
							// selection info
							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
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
						.setPositiveButton(okButtonTitle,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										// convert to int array

										ArrayList<String> selections = new ArrayList<String>();
										for (Integer s : selectedItems) {
											selections.add(options[s]);
										}

										KrollDict data = new KrollDict();
										data.put(
												"indexes",
												selectedItems
														.toArray(new Integer[selectedItems
																.size()]));
										data.put("selections", selections
												.toArray(new String[selections
														.size()]));
										data.put("result", resultList);
										if (hasListeners("click"))
											fireEvent("click", data);
									}
								});

				if (cancellable == true) {

					// cancel returns nothing
					getBuilder().setNegativeButton(cancelButtonTitle,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									fireEvent("cancel", new KrollDict());
								}
							});
				} else {
					getBuilder().setCancelable(false);
				}
			}

		}

		public void show() {
			getBuilder().create().show();
			builder = null;
			Log.d(LCAT, "show Dialog");
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
