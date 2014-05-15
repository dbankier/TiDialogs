package yy.tidialogs;

import java.util.Calendar;
import java.util.Date;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiUIHelper;
import org.appcelerator.titanium.view.TiUIView;

import android.R;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.widget.DatePicker;

@Kroll.proxy(creatableInModule = TidialogsModule.class)
public class DatePickerProxy extends TiViewProxy {
	private class BasicDatePicker extends TiUIView {

		private int year;
		private int month;
		private int day;

		private String okButtonTitle;
		private String cancelButtonTitle;

		public BasicDatePicker(TiViewProxy proxy) {
			super(proxy);

		}

		private DatePickerDialog getDialog() {
			DatePickerDialog picker = new DatePickerDialog(this.proxy.getActivity(),
						new DatePickerDialog.OnDateSetListener() {
							// when dialog box is closed, below method will be
							// called.
							public void onDateSet(DatePicker view,
									int selectedYear, int selectedMonth,
									int selectedDay) {
								year = selectedYear;
								month = selectedMonth;
								day = selectedDay;

								KrollDict data = new KrollDict();

									Calendar calendar = Calendar.getInstance();
									calendar.set(Calendar.YEAR, year);
									calendar.set(Calendar.MONTH, month);
									calendar.set(Calendar.DAY_OF_MONTH, day);
									Date value = calendar.getTime();

									data.put("value", value);
									data.put("year", year);
									data.put("month", month);
									data.put("day", day);
                                    fireEvent("click", data);		

							}
						}, year, month, day);
			picker.setCanceledOnTouchOutside(false);

			picker.setButton(DialogInterface.BUTTON_POSITIVE, okButtonTitle, picker);

			picker.setButton(DialogInterface.BUTTON_NEGATIVE, cancelButtonTitle,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						fireEvent("cancel", new KrollDict());
					}
				});

			return picker;
		}

		@Override
		public void processProperties(KrollDict d) {
			super.processProperties(d);
			Calendar c = Calendar.getInstance();
			if (d.containsKey("value")) {
				c.setTime((Date) d.get("value"));
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH);
				day = c.get(Calendar.DAY_OF_MONTH);
			} else {
				if (d.containsKey("year")) {
					year = d.getInt("year");
				} else {
					year = c.get(Calendar.YEAR);
				}
				if (d.containsKey("month")) {
					month = d.getInt("month");
				} else {
					month = c.get(Calendar.MONTH);
				}
				if (d.containsKey("day")) {
					day = d.getInt("day");
				} else {
					day = c.get(Calendar.DAY_OF_MONTH);
				}
			}

			if (d.containsKey("okButtonTitle")) {
				okButtonTitle = d.getString("okButtonTitle");
			} else {
				okButtonTitle =  this.proxy.getActivity().getApplication().getResources().getString(R.string.ok);
			}
			if (d.containsKey("cancelButtonTitle")) {
				cancelButtonTitle = d.getString("cancelButtonTitle");
			} else {
				cancelButtonTitle = this.proxy.getActivity().getApplication().getResources().getString(R.string.cancel);
			}
		}

		public void show() {
			getDialog().show();
		}

	}

	public DatePickerProxy() {
		super();
	}

	@Override
	public TiUIView createView(Activity activity) {
		return new BasicDatePicker(this);
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
				BasicDatePicker d = (BasicDatePicker) getOrCreateView();
				d.show();
			}
		});
	}
}