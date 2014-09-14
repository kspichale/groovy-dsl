package com.kspichale.goovy.dsl;

import org.codehaus.groovy.control.CompilerConfiguration
import org.junit.Test

import com.kspichale.groovy.dsl.Order
import com.kspichale.groovy.dsl.OrderPosition

public class delegateExample {

	@Test
	public void createOrderWithDsl() throws Exception {

		Order order = new Order()
		OrderPosition pos1 = new OrderPosition()
		pos1.product = 'Product A'
		pos1.price = 42.00
		order.addPosition(pos1)
		OrderPosition pos2 = new OrderPosition()
		pos2.product = 'Product B'
		pos2.price = 8.15
		order.addPosition(pos2)

		CompilerConfiguration conf = new CompilerConfiguration();
		conf.setScriptBaseClass(DslScript.class.getName());
		GroovyShell gs = new GroovyShell(new Binding(), conf);
		Script s = gs.parse('''
			order {
				orderPosition {
					product 'Product A'
					price 42.00
				}
				orderPosition {
					product 'Product B'
					price 8.15
				}
			}''');
		Order generatedOrder = s.run();
		assert order == generatedOrder
	}
}

abstract class DslScript extends Script {

	def order(Closure c) {
		c.delegate = new OrderScriptBuilder()
		c()
	}
}

class OrderScriptBuilder {
	Order order = new Order()
	OrderPosition position

	def orderPosition(closure) {
		position = new OrderPosition()
		order.addPosition(position)
		closure()
		order
	}

	def product(product) {
		position.product = product
	}

	def price(price) {
		position.price = price
	}
}



