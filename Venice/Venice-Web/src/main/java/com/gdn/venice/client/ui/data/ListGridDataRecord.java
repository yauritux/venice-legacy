package com.gdn.venice.client.ui.data;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Used as a placeholder for client managed list grid data.
 * The column names etc. will be replaced later by the component using the class.
 *
 */
public class ListGridDataRecord extends ListGridRecord {

    public ListGridDataRecord() {
    }

    public ListGridDataRecord(String[] data) {
    	try {
	    	if (data[0] != null)  {
	    		setColumn0(data[0]);
	    	}
	    	if (data[1] != null)  {
	    		setColumn1(data[1]);
	    	}
	    	if (data[2] != null)  {
	    		setColumn2(data[2]);
	    	}
	    	if (data[3] != null)  {
	    		setColumn3(data[3]);
	    	}
	    	if (data[4] != null)  {
	    		setColumn4(data[4]);
	    	}
	    	if (data[5] != null)  {
	    		setColumn5(data[5]);
	    	}
	    	if (data[6] != null)  {
	    		setColumn6(data[6]);
	    	}
	    	if (data[7] != null)  {
	    		setColumn7(data[7]);
	    	}
	    	if (data[8] != null)  {
	    		setColumn8(data[8]);
	    	}
    	} catch (ArrayIndexOutOfBoundsException e) {
    		
    	}
    }

    public void setColumn0(String value) {
        setAttribute("column0", value);
    }
    
    public String gtColumn0() {
        return getAttributeAsString("column0");
    }

    public void setColumn1(String value) {
        setAttribute("column1", value);
    }
    
    public String gtColumn1() {
        return getAttributeAsString("column1");
    }
    
    public void setColumn2(String value) {
        setAttribute("column2", value);
    }
    
    public String gtColumn2() {
        return getAttributeAsString("column2");
    }
    
    public void setColumn3(String value) {
        setAttribute("column3", value);
    }
    
    public String gtColumn3() {
        return getAttributeAsString("column3");
    }
    
    public void setColumn4(String value) {
        setAttribute("column4", value);
    }
    
    public String gtColumn4() {
        return getAttributeAsString("column4");
    }
    
    public void setColumn5(String value) {
        setAttribute("column5", value);
    }
    
    public String gtColumn5() {
        return getAttributeAsString("column5");
    }
    
    public void setColumn6(String value) {
        setAttribute("column6", value);
    }
    
    public String gtColumn6() {
        return getAttributeAsString("column6");
    }
    
    public void setColumn7(String value) {
        setAttribute("column7", value);
    }
    
    public String gtColumn7() {
        return getAttributeAsString("column7");
    }
    
    public void setColumn8(String value) {
        setAttribute("column8", value);
    }
    
    public String gtColumn8() {
        return getAttributeAsString("column8");
    }

    
}
