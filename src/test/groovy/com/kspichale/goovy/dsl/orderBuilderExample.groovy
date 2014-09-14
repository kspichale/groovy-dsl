package com.kspichale.goovy.dsl

import com.kspichale.groovy.dsl.Order
import com.kspichale.groovy.dsl.OrderPosition

class OrderBuilder extends BuilderSupport {
	def createNode(name) {
		switch(name) {
			case "order":
				return new Order()
			case "orderPosition":
				return new OrderPosition()
		}
	}

	def createNode(name, parent) {
		Object result = createNode(name)
		if(parent instanceof Order && result instanceof OrderPosition) {
			parent.addToPositions(result)
		}
		return result
	}

	def createNode(name, Map attributes) {
		switch(name) {
			case "order":
				return new Order(attributes)
			case "orderPosition":
				return new OrderPosition(attributes)
		}
	}

	def createNode(name, Map attributes, value) {
		Object result = createNode(name, attributes)
		if(value instanceof Order && result instanceof OrderPosition) {
			value.addToPositions(result)
		}
		return result
	}

	void setParent(parent, child) {
		if(child instanceof OrderPosition && parent instanceof Order) {
			parent.addPosition(child)
		}
	}
}

def order = new OrderBuilder().order {
	orderPosition(product:"Product A", price:42.00)
	orderPosition(product:"Product B", price:8.15)
}

class OrderFactory extends AbstractFactory {
	public boolean isLeaf() {
		return false
	}
	public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
		Order order = null
		if(attributes!=null) {
			order = new Order(attributes)
		} else {
			order = new Order()
		}
		return order
	}
}

class OrderPositionFactory extends AbstractFactory {
	public boolean isLeaf() {
		return true
	}
	public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
		OrderPosition position = null
		if(attributes!=null) {
			position = new OrderPosition(attributes)
		} else {
			position = new OrderPosition()
		}
		if(value!=null && value instanceof Order) {
			value.addPosition(position)
		}
		return position
	}
	public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
		if(parent!=null && parent instanceof Order) {
			parent.addPosition(child)
		}
	}
}

class OrderFactoryBuilder extends FactoryBuilderSupport {
	public OrderFactoryBuilder(boolean init = true) {
		super(init)
	}

	def registerObjectFactories() {
		registerFactory("order", new OrderFactory())
		registerFactory("orderPosition", new OrderPositionFactory())
	}
}

def order2 = new OrderFactoryBuilder().order {
	orderPosition(product:"Product A", price:42.00)
	orderPosition(product:"Product B", price:8.15)
}

assert order == order2
