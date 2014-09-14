package com.kspichale.groovy.dsl

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Order {
	List<OrderPosition> positions = []
	void addPosition(OrderPosition o) {
		positions.add(o)
	}
}
