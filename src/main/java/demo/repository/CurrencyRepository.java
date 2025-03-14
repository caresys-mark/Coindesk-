package demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Integer>{
	Currency findByCode(String code);

}
