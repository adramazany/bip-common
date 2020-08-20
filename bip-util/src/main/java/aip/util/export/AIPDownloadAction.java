package aip.util.export;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DownloadAction;

import aip.util.NVL;

public class AIPDownloadAction extends DownloadAction {

	protected StreamInfo getStreamInfo(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String contentType = NVL.getString(request.getAttribute("contentType"));//"application/pdf";
        final InputStream inputStream  = (InputStream) request.getAttribute("inputStream");// Get the bytes from somewhere
        
        return new StreamInfo() {
			public InputStream getInputStream() throws IOException {
				return inputStream;
			}
			public String getContentType() {
				return contentType;
			}
		};
	}

}
