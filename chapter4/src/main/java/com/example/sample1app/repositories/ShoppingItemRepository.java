package com.example.sample1app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sample1app.ShoppingItem;

@Repository
public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long> {
	public Optional<ShoppingItem> findById(long name);
	public List<ShoppingItem> findByItemOrMoneyGreaterThan(String item, Integer money);
	public List<ShoppingItem> findByMoney(Integer money);
}
