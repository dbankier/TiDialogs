package yy.tidialogs;

import java.util.Calendar;
import java.util.Date;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

import android.R;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.os.Build;
import android.widget.DatePicker;

import ti.modules.titanium.ui.widget.picker.TiDatePickerDialog;

@Kroll.proxy(creatableInModule = TidialogsModule.class)
public class DatePickerProxy extends BaseDialogProxy
{
	private class BasicDatePicker extends BaseUIDialog
	{

		private int year;
		private int month;
		private int day;

		private Date maxDate;
		private Date minDate;

		private String okButtonTitle;
		private String cancelButtonTitle;

		public BasicDatePicker(TiViewProxy proxy)
		{
			super(proxy);
		}

		protected DatePickerDialog getDialog()
		{
			OnDateSetListener dateSetListener = new OnDateSetListener() {
				// when dialog box is closed, below method will be
				// called.
				public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay)
				{
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
			};
			DatePickerDialog picker;

			if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
				&& (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)) {
				picker = new TiDatePickerDialog(proxy.getActivity(), dateSetListener, year, month, day);
			} else {
				picker = new DatePickerDialog(TiApplication.getAppCurrentActivity(), dateSetListener, year, month, day);
			}

			if (minDate != null) {
				picker.getDatePicker().setMinDate(trimDate(minDate).getTime());
			}

			if (maxDate != null) {
				picker.getDatePicker().setMaxDate(trimDate(maxDate).getTime());
			}

			picker.setCanceledOnTouchOutside(false);

			picker.setButton(DialogInterface.BUTTON_POSITIVE, okButtonTitle, picker);

			picker.setButton(DialogInterface.BUTTON_NEGATIVE, cancelButtonTitle, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					fireEvent("cancel", new KrollDict());
				}
			});

			dialog = picker;
			return picker;
		}

		@Override
		public void processProperties(KrollDict d)
		{
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

			if (d.containsKey(TiC.PROPERTY_MIN_DATE)) {
				minDate = (Date) d.get(TiC.PROPERTY_MIN_DATE);
			}
			if (d.containsKey(TiC.PROPERTY_MAX_DATE)) {
				maxDate = (Date) d.get(TiC.PROPERTY_MAX_DATE);
			}

			if (d.containsKey("okButtonTitle")) {
				okButtonTitle = d.getString("okButtonTitle");
			} else {
				okButtonTitle = this.proxy.getActivity().getApplication().getResources().getString(R.string.ok);
			}
			if (d.containsKey("cancelButtonTitle")) {
				cancelButtonTitle = d.getString("cancelButtonTitle");
			} else {
				cancelButtonTitle = this.proxy.getActivity().getApplication().getResources().getString(R.string.cancel);
			}
		}
	}

	public DatePickerProxy()
	{
		super();
	}

	@Override
	public TiUIView createView(Activity activity)
	{
		return new BasicDatePicker(this);
	}

	@Override
	public void handleCreationDict(KrollDict options)
	{
		super.handleCreationDict(options);
	}

	/**
	 * Trim hour, minute, second and millisecond from the date
	 * @param inDate input date
	 * @return return the trimmed date
	 */
	public static Date trimDate(Date inDate)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(inDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}