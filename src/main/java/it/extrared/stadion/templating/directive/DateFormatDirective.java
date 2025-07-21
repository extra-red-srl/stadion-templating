package it.extrared.stadion.templating.directive;

import it.extrared.stadion.utils.DateTimeUtils;

public class DateFormatDirective extends FunctionDirective {

    private final String src;
    private final String trg;

    public DateFormatDirective(String srcFmt, String trgFmt) {
        this.src = srcFmt;
        this.trg = trgFmt;
    }

    @Override
    public Object run(Object object) {
        return DateTimeUtils.formatDate(DateTimeUtils.parseDate(object.toString(), src), trg);
    }
}
