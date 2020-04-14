package org.dieschnittstelle.ess.basics;


import org.dieschnittstelle.ess.basics.annotations.AnnotatedStockItemBuilder;
import org.dieschnittstelle.ess.basics.annotations.StockItemProxyImpl;

import java.lang.reflect.Field;

import static org.dieschnittstelle.ess.utils.Utils.*;

public class ShowAnnotations {

	public static void main(String[] args) {
		// we initialise the collection
		StockItemCollection collection = new StockItemCollection(
				"stockitems_annotations.xml", new AnnotatedStockItemBuilder());
		// we load the contents into the collection
		collection.load();

		for (IStockItem consumable : collection.getStockItems()) {
			;
			showAttributes(((StockItemProxyImpl)consumable).getProxiedObject());
		}

		// we initialise a consumer
		Consumer consumer = new Consumer();
		// ... and let them consume
		consumer.doShopping(collection.getStockItems());
	}

	private static void showAttributes(Object consumable) {
		StringBuilder builder = new StringBuilder();
		builder.append("{" + consumable.getClass().getSimpleName() + " ");
		for(Field f : consumable.getClass().getDeclaredFields()){
			f.setAccessible(true);
			try {
				builder.append(f.getName() + ":" + f.get(consumable) + ",");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		builder.delete(builder.length() -1, builder.length());
		builder.append("}");
		show(builder.toString());
	}

}
