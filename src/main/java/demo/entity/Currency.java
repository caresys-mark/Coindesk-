package demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Currency {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; 
	
	@Column(name = "updated")
	private String updated;

	@Column(name = "updated_iso")
	private String updatedIso;

	@Column(name = "updated_uk")
	private String updatedUk;

	@Column(name = "disclaimer")
	private String disclaimer;

	@Column(name = "chart_name")
	private String chartName;

	@Column(name = "code")
	private String code;

	@Column(name = "symbol")
	private String symbol;

	@Column(name = "rate")
	private Double rate;

	@Column(name = "description")
	private String description;

	@Column(name = "rate_float")
	private Double rateFloat; 

}
