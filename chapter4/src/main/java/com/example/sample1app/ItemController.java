package com.example.sample1app;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.sample1app.repositories.ShoppingItemRepository;

import jakarta.transaction.Transactional;

@Controller
public class ItemController {
	
	//SHoppingItemをインスタンス
	@Autowired
	ShoppingItemRepository repository;
	
	//トップページ
	@GetMapping("/")
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("title", "商品管理");
		mav.addObject("search", "費目または金額を入力してください");
		Iterable<ShoppingItem> list = repository.findAll();
		mav.addObject("data", list);
		return mav;
	}
	
	//検索ページ
	@PostMapping("/search")
	public ModelAndView search(
			@RequestParam("item") String item,
			@RequestParam("money") Integer money,
			ModelAndView mav) {
		mav.setViewName("search");
		Iterable<ShoppingItem> data = repository.findByItemOrMoneyGreaterThan(item, money);
		mav.addObject("title", "検索結果");
		mav.addObject("data", data);
		return mav;
	}
	
	//商品登録ページ（GET）
	@GetMapping("/create")
	public ModelAndView create(
			@ModelAttribute("formModel") ShoppingItem ShoppingItem,
			ModelAndView mav) {
		mav.setViewName("create");
		mav.addObject("title", "商品登録");
		mav.addObject("msg", "商品を登録してください");
		return mav;
	}
	
	//商品登録ページ（POST)
	@PostMapping("/create")
	@Transactional
	public ModelAndView form(
			@ModelAttribute("formModel") @Validated ShoppingItem ShoppingItem,
			BindingResult result,
			@RequestParam(value = "item", required = false) String item02,
			ModelAndView mav) {
		ModelAndView res = null;
		
		if(!result.hasErrors()) {
			repository.saveAndFlush(ShoppingItem);
			res = new ModelAndView("redirect:/");
			System.out.println(result.getFieldErrors());
		} else {
			mav.setViewName("create");
			mav.addObject("title", "商品登録");
			mav.addObject("msg", "エラーを確認してください");
			Iterable<ShoppingItem> list = repository.findAll();
			mav.addObject("datalist", list);
			res = mav;

		}
		return res;
	}
	
	
	//商品詳細（GET）
	@GetMapping("/read/{id}")
	public ModelAndView read(
			@PathVariable int id,
			@ModelAttribute ShoppingItem ShoppingItem,
			ModelAndView mav) {
		Optional<ShoppingItem> data = repository.findById((long)id);
		mav.setViewName("read");
		mav.addObject("formModel", data.get());
		return mav;
	}
	
	@PostMapping("/update")
	@Transactional
	public ModelAndView update(
			@ModelAttribute ShoppingItem ShoppingItem,
			ModelAndView mav
			) {
		repository.saveAndFlush(ShoppingItem);
		//idを取得（long型で設定しているためlong型で宣言）
		long id = ShoppingItem.getId();
		//取得したidを元にリダイレクト
		return new ModelAndView("redirect:/read/" + id);
	}

	
	//データ削除
	@GetMapping("/delete/{id}")
	public ModelAndView delete(
			@PathVariable long id,
			ModelAndView mav) {
		repository.deleteById(id);
		return new ModelAndView("redirect:/");
	}
	
	//ダミーデータ
	@PostConstruct
	public void init() {
		ShoppingItem s1 = new ShoppingItem();
		s1.setName("カレー");
		s1.setItem("洋食");
		s1.setMoney(800);
		s1.setMemo("人気のカレー屋さん");
		repository.saveAndFlush(s1);
		
		ShoppingItem s2 = new ShoppingItem();
		s2.setName("とんかつ定食");
		s2.setItem("和食");
		s2.setMoney(1200);
		s2.setMemo("スタミナがほしいあなたに");
		repository.saveAndFlush(s2);
		
		ShoppingItem s3 = new ShoppingItem();
		s3.setName("海鮮丼");
		s3.setItem("海鮮");
		s3.setMoney(1500);
		s3.setMemo("いろんな魚が入っている");
		repository.saveAndFlush(s3);
		
		ShoppingItem s4 = new ShoppingItem();
		s4.setName("ミックスジュース");
		s4.setItem("飲料");
		s4.setMoney(450);
		s4.setMemo("いろんなフルーツが入ってる");
		repository.saveAndFlush(s4);
	}
	
}
