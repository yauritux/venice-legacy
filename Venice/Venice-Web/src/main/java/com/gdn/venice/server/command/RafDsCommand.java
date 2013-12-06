package com.gdn.venice.server.command;

import com.gdn.venice.server.data.RafDsResponse;

public interface RafDsCommand {
	public final String DataSource = "DataSource";
	
	public RafDsResponse execute();
}
