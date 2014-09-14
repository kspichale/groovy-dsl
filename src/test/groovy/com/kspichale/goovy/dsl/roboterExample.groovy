package com.kspichale.goovy.dsl

class Rotation {
	int angle
	def right(closure) {
		def velocity = closure()
		println "turn rigth $angle degree $velocity"
	}
}

def turn(arg) {
	arg
}

slowly = { -> "slowly" }

Integer.metaClass.getDegree = { -> new Rotation(angle: delegate) }

// --------------------

turn(20.degree).right(slowly)

turn 20.degree right slowly