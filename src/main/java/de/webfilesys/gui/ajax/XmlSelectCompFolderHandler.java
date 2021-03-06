package de.webfilesys.gui.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.w3c.dom.Element;

import de.webfilesys.gui.xsl.XslSyncCompareHandler;
import de.webfilesys.util.XmlUtil;

/**
 * @author Frank Hoehnel
 */
public class XmlSelectCompFolderHandler extends XmlRequestHandlerBase
{
	public static final String SESSION_ATTRIB_COMP_SOURCE = "compSourceFolder";
	public static final String SESSION_ATTRIB_COMP_TARGET = "compTargetFolder";
	
	public XmlSelectCompFolderHandler(
    		HttpServletRequest req, 
    		HttpServletResponse resp,
            HttpSession session,
            PrintWriter output, 
            String uid)
	{
        super(req, resp, session, output, uid);
	}
	
	protected void process()
	{
        String cmd = getParameter("cmd");
        
        if ((cmd != null) && cmd.equals("deselect"))
        {
            session.removeAttribute(SESSION_ATTRIB_COMP_SOURCE);
            session.removeAttribute(SESSION_ATTRIB_COMP_TARGET);
            session.removeAttribute(XslSyncCompareHandler.SESSION_ATTRIB_SYNCHRONIZE_ITEMS);
            
            Element resultElement = doc.createElement("result");
            
            XmlUtil.setChildText(resultElement, "success", "true");
                
            doc.appendChild(resultElement);
            
            this.processResponse();

            return;
        }
        
		String path = getParameter("path");

		if (!checkAccess(path))
		{
			return;
		}

		boolean targetSelected = false;
		
		String compSourceFolder = (String) session.getAttribute(SESSION_ATTRIB_COMP_SOURCE);
		
		if (compSourceFolder == null) 
		{
			session.setAttribute(SESSION_ATTRIB_COMP_SOURCE, path);
		}
		else
		{
			session.setAttribute(SESSION_ATTRIB_COMP_TARGET, path);
			targetSelected = true;
		}
		
		Element resultElement = doc.createElement("result");
		
		if (targetSelected)
		{
			XmlUtil.setChildText(resultElement, "success", "targetSelected");
		}
		else
		{
			String resultMsg = getResource("label.selectCompSourceResult", "Select compare from the context menu of the target folder!");
			
			XmlUtil.setChildText(resultElement, "message", resultMsg);

			XmlUtil.setChildText(resultElement, "success", "sourceSelected");
		}
			
		doc.appendChild(resultElement);
		
		this.processResponse();
	}
}
