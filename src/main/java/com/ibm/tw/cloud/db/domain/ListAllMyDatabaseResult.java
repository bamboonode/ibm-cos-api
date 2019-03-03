package com.ibm.tw.cloud.db.domain;

import java.util.ArrayList;
import java.util.List;

public class ListAllMyDatabaseResult {
	private List<Deployable> deployables = new ArrayList<>();

	public List<Deployable> getDeployables() {
		return deployables;
	}

	public void setDeployables(List<Deployable> deployables) {
		this.deployables = deployables;
	}
	
}
