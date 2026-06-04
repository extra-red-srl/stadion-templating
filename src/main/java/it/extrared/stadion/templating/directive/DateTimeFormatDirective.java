/*
 * Copyright 2026 Extrared
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.extrared.stadion.templating.directive;

import it.extrared.stadion.utils.DateTimeUtils;

/** Formats a date-time value using configurable source and target date-time patterns. */
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
