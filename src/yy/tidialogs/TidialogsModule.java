package yy.tidialogs;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;

@Kroll.module(name = "Tidialogs", id = "yy.tidialogs")
public class TidialogsModule extends KrollModule {
	public static final String LCAT = "TiDialogs🎛";

	public TidialogsModule() {
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {

	}
}
