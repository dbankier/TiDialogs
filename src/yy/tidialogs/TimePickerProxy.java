package yy.tidialogs;

import java.util.Calendar;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiUIHelper;
import org.appcelerator.titanium.view.TiUIView;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import android.widget.TimePicker;

@Kroll.proxy(creatableInModule = TidialogsModule.class)
public class TimePickerProxy extends TiViewProxy {
	private class BasicDatePicker extends TiUIView {

		private TimePickerDialog dialog;
		int hour;
		int minute;

		public BasicDatePicker(TiViewProxy proxy) {
			super(proxy);

		}

		private TimePickerDialog getDialog() {
			if (dialog == null) {
				dialog = new TimePickerDialog(this.proxy.getActivity(),
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker selectedTime,
									int selectedHour, int selectedMinute) {
								// TODO Auto-generated method stub

								hour = selectedHour;
								minute = selectedMinute;

								KrollDict data = new KrollDict();
								data.put("hour", hour);
								data.put("minute", minute);
								fireEvent("click", data);
							}
						}, hour, minute, DateFormat.is24HourFormat(this.proxy
								.getActivity()));
			}
			return dialog;

		}

		@Override
		public void processProperties(KrollDict d) {
			super.processProperties(d);
			final Calendar c = Calendar.getInstance();
			if (d.containsKey("hour")) {
				hour = d.getInt("hour");
			} else {
				hour = c.get(Calendar.HOUR_OF_DAY);
			}
			if (d.containsKey("minute")) {
				minute = d.getInt("minute");
			} else {
				minute = c.get(Calendar.MINUTE);
			}
		}

		public void show() {
			getDialog().show();
		}

		public void hide() {
			getDialog().dismiss();
		}

	}

	public TimePickerProxy() {
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