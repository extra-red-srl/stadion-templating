package it.extrared.stadion.templating.directive;

import it.extrared.stadion.utils.DateTimeUtils;

public class DateTimeFormatDirective extends FunctionDirective {
    private final String src;
    private final String trg;

    public DateTimeFormatDirective(String srcFmt, String trgFmt) {
        this.src = srcFmt;
        this.trg = trgFmt;
    }

    @Override
    public Object run(Object object) {
        return DateTimeUtils.formatDateTime(
                DateTimeUtils.parseDateTime(object.toString(), src), trg);
    }
}
