package yy.tidialogs;

import java.util.Calendar;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiUIHelper;
import org.appcelerator.titanium.view.TiUIView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.DatePicker;


@Kroll.proxy(creatableInModule = TidialogsModule.class)
public class DatePickerProxy extends TiViewProxy {
	private class BasicDatePicker extends TiUIView {
		private int year;
		private int month;
		private int day;
		private DatePickerDialog dialog;

		public BasicDatePicker(TiViewProxy proxy) {
			super(proxy);

		}

		private DatePickerDialog getDialog() {
			if (dialog == null) {
				dialog = new DatePickerDialog(this.proxy.getActivity(),
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
								data.put("year", year);
								data.put("month", month);
								data.put("day", day);
								fireEvent("click", data);
							}
						}, year, month, day);
			}
			return dialog;

		}

		@Override
		public void processProperties(KrollDict d) {
			super.processProperties(d);
			final Calendar c = Calendar.getInstance();
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

		public void show() {
			getDialog().show();
		}

		public void hide() {
			getDialog().dismiss();
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

	@Override
	protected void handleHide(KrollDict options) {
		super.handleHide(options);

		BasicDatePicker d = (BasicDatePicker) getOrCreateView();
		d.hide();
	}
}