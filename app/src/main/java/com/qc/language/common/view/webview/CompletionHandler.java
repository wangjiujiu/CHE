/*
 * Created by ConDey
 * Company eazytec.com
 */

package com.qc.language.common.view.webview;


public interface CompletionHandler<T> {
    void complete(T retValue);

    void complete();

    void setProgressData(T value);
}
