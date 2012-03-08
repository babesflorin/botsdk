package com.sarxos.skbot.strategy;

import com.sarxos.skbot.AbstractStrategy;
import com.sarxos.skbot.Consol;
import com.sarxos.skbot.Merchant;
import com.sarxos.skbot.SHKException;
import com.sarxos.skbot.Scout;
import com.sarxos.skbot.Stash;

/**
 * This strategy will search for nearest stash, forage it and sell corresponding 
 * good in stock exchange.
 * 
 * @author Bartosz Firyn (SarXos)
 */
public class ScoutAndSellStrategy extends AbstractStrategy {

	@Override
	public void start() throws SHKException {
		do {
			for (Scout scout : getScouts()) {
				if (!isRunning()) {
					break;
				}
				Consol.info("Forage from " + scout.getVillage().getName());
				if (scout.forage()) {
					Stash stash = scout.getTarget();
					if (stash == null) {
						continue;
					}
					if (!stash.isPouch()) { 
						for (Merchant merchant : getMerchants()) {
							if (!isRunning()) {
								break;
							}
							if (merchant.getVillage() == scout.getVillage()) {
								Consol.info("Sell " + stash.getGoods() + " in " + scout.getVillage().getName());
								if (merchant.sell(stash.getGoods())) {
									break;
								}
							}
						}
					}
				}
			}
		} while (isRunning());
	}
	
	@Override
	public String getName() {
		return "Scout And Sell Strategy";
	}
}
