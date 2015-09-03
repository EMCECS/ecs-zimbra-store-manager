package com.emc.ecs.zimbra.integration.util;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;

/**
 * <p>
 * Logs upload progress to ECS while writing into bucket.
 * </p>
 * <p>
 * This ProgressListener is used for debugging purposes only.
 * It works only of logging level is set to <i>DEBUG</i>. So
 * in order to enable this feature you must set:
 * <b>log4j.logger.zimbra.store=DEBUG</b>
 * </p>
 * <p>
 * For more information see
 * <a href="http://wiki.zimbra.com/wiki/Log_Files">
 * Zimbra documentation for Log Files
 * </a>
 * </p>
 */
public class EcsProgressListener implements ProgressListener {

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        int eventCode = progressEvent.getEventCode();
        long bytesTransferred = progressEvent.getBytesTransferred();
        EcsLogger.debug(
                String.format("ProgressEvent: code - %s, bytesTransferred - %s",
                        getEventName(eventCode),
                        bytesTransferred)
        );
    }

    private String getEventName(int eventCode) {
        switch (eventCode) {
            case ProgressEvent.PREPARING_EVENT_CODE:
                return "Preparing";
            case ProgressEvent.STARTED_EVENT_CODE:
                return "Started";
            case ProgressEvent.COMPLETED_EVENT_CODE:
                return "Completed";
            case ProgressEvent.FAILED_EVENT_CODE:
                return "Failed";
            case ProgressEvent.CANCELED_EVENT_CODE:
                return "Canceled";
            case ProgressEvent.RESET_EVENT_CODE:
                return "Reset";
            case ProgressEvent.PART_STARTED_EVENT_CODE:
                return "Multipart upload part started";
            case ProgressEvent.PART_COMPLETED_EVENT_CODE:
                return "Multipart upload part completed";
            case ProgressEvent.PART_FAILED_EVENT_CODE:
                return "Multipart upload part failed";
        }

        return "Unknown event: " + eventCode;
    }

}
