package com.kspichale.goovy.dsl

def myObject = "myObject"

def outerClosure = { self ->
	// this und owner zeigen beide auf das Skript mit Binding als umgebenden Kontext
	assert this == owner
	// delegate weicht von owner ab, weil delegate auf myObject gesetzt wurde 
	assert delegate == myObject
	
	def innerClosure = {
		// hier unterscheiden sich owner und this
		assert this != owner
		// this in der inneren Closure zeigt auf das this der outerClosure
		// d.h. this entspricht hier dem owner.owner
		assert this == owner.owner
		assert this == owner.thisObject
		// owner und delegate zeigen beide self
		assert owner == self
		assert delegate == self
	}
	
	// innerhalb der outerClousure wird die eingebettete innerClosure aufgerufen
	innerClosure()
}

outerClosure.delegate = myObject
outerClosure(outerClosure)

