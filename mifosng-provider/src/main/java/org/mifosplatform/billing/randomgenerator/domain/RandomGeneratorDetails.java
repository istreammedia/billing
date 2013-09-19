package org.mifosplatform.billing.randomgenerator.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "b_pin_details"/*, uniqueConstraints = @UniqueConstraint(name = "serial_no_key", columnNames = { "serial_no" })*/)
public class RandomGeneratorDetails extends AbstractPersistable<Long> {
	
	@ManyToOne
    @JoinColumn(name="pin_id")
    private RandomGenerator randomGenerator;
	
	@Column(name = "pin_no")
	private String pinNo;
	
	@Column(name = "serial_no", nullable = false)
	private Long serialNo;
	
	
	public RandomGeneratorDetails(){
		
	}

	public RandomGeneratorDetails(String name, Long no) {
		// TODO Auto-generated constructor stub
		this.pinNo=name;
		this.serialNo=no;
	}

	public void update(RandomGenerator randomGenerator) {
		this.randomGenerator=randomGenerator;
	}

	public RandomGenerator getRandomGenerator() {
		return randomGenerator;
	}
	

}
