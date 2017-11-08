package com.wjika.cardstore.network.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wjika.cardstore.network.entities.BranchEntity;
import com.wjika.cardstore.network.entities.CommodityEntity;
import com.wjika.cardstore.network.entities.ConsumptionEntity;
import com.wjika.cardstore.network.entities.Entity;
import com.wjika.cardstore.network.entities.FinancePageEntity;
import com.wjika.cardstore.network.entities.MerDataEntity;
import com.wjika.cardstore.network.entities.MessageEntity;
import com.wjika.cardstore.network.entities.OrderManagerEntity;
import com.wjika.cardstore.network.entities.PageEntity;
import com.wjika.cardstore.network.entities.SelectPhotoEntity;
import com.wjika.cardstore.network.entities.StatisticsEntity;
import com.wjika.cardstore.network.entities.UpdateVersionEntity;
import com.wjika.cardstore.network.entities.UserDetailEntity;
import com.wjika.cardstore.network.entities.UserEntity;
import com.wjika.cardstore.network.json.GsonObjectDeserializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by jacktian on 15/8/30.
 * json解析类
 */
public class Parsers {

	public static Gson gson = GsonObjectDeserializer.produceGson();

	/**
	 * @param data json串
	 * @return 请求结果基本信息
	 */
	public static Entity parseResult(String data) {
		return gson.fromJson(data, new TypeToken<Entity>() {}.getType());
	}

	public static UserEntity parseUser(String data) {
		UserEntity userEntity = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			String json = jsonObject.optString("val");
			userEntity = gson.fromJson(json, new TypeToken<UserEntity>() {
			}.getType());
			if (userEntity != null) {
				userEntity.setRspCode(jsonObject.optString("rspCode"));
				userEntity.setRspMsg(jsonObject.optString("rspMsg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return userEntity;
	}

	public static List<BranchEntity> parseBranch(String data) {
		List<BranchEntity> branchEntities = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			jsonObject = new JSONObject(jsonObject.optString("val"));
			branchEntities = gson.fromJson(jsonObject.optString("datas"), new TypeToken<List<BranchEntity>>(){}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return branchEntities;
	}

	public static MerDataEntity parseMerData(String data){
		MerDataEntity merDataEntity = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			merDataEntity = gson.fromJson(jsonObject.optString("val"), new TypeToken<MerDataEntity>(){
			}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return merDataEntity;
	}

	public static PageEntity<MessageEntity> parseMessage(String data){
		PageEntity<MessageEntity> pageEntity = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			pageEntity = gson.fromJson(jsonObject.optString("val"), new TypeToken<PageEntity<MessageEntity>>(){
			}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return pageEntity;
	}

	public static CommodityEntity parseCommodity(String data){
		CommodityEntity commodityEntity = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			commodityEntity = gson.fromJson(jsonObject.optString("val"), new TypeToken<CommodityEntity>(){
			}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return commodityEntity;
	}

	public static OrderManagerEntity parseOrderManager(String data){
		OrderManagerEntity orderManagerEntity = null;
		try {
			JSONObject jsonObject=new JSONObject(data);
			orderManagerEntity = gson.fromJson(jsonObject.optString("val"), new TypeToken<OrderManagerEntity>(){}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return orderManagerEntity;
	}

	public static UpdateVersionEntity parseUpdateVersion(String data){
		UpdateVersionEntity updateVersionEntity = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			updateVersionEntity = gson.fromJson(jsonObject.optString("val"), UpdateVersionEntity.class);
			if (updateVersionEntity != null) {
				updateVersionEntity.setRspCode(jsonObject.optString("rspCode"));
				updateVersionEntity.setRspMsg(jsonObject.optString("rspMsg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return updateVersionEntity;
	}

	public static FinancePageEntity parseFinanceList(String data){
		FinancePageEntity financePageEntity = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			financePageEntity = gson.fromJson(jsonObject.optString("val"), new TypeToken<FinancePageEntity>(){}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return financePageEntity;
	}

	public static SelectPhotoEntity parsePhotoList(String data){
		SelectPhotoEntity selectPhotoEntity = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			selectPhotoEntity = gson.fromJson(jsonObject.optString("val"), new TypeToken<SelectPhotoEntity>(){
			}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return selectPhotoEntity;
	}

	public static SelectPhotoEntity parseSelectPhoto(String data){
		SelectPhotoEntity selectPhotoEntity = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			selectPhotoEntity = gson.fromJson(jsonObject.optString("val"), new TypeToken<SelectPhotoEntity>(){
			}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return selectPhotoEntity;
	}

	public static PageEntity<ConsumptionEntity> parseConsumption(String data){
		PageEntity<ConsumptionEntity> pageEntity = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			pageEntity = gson.fromJson(jsonObject.optString("val"), new TypeToken<PageEntity<ConsumptionEntity>>(){
			}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return pageEntity;
	}

	public static StatisticsEntity parseStatistics(String data) {
		StatisticsEntity statisticsEntity = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			statisticsEntity = gson.fromJson(jsonObject.optString("val"), new TypeToken<StatisticsEntity>() {
			}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return statisticsEntity;
	}

	public static UserDetailEntity parseUserDetail(String data) {
		UserDetailEntity userEntity = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			String json = jsonObject.optString("val");
			userEntity = gson.fromJson(json, new TypeToken<UserDetailEntity>() {
			}.getType());
			if (userEntity != null) {
				userEntity.setRspCode(jsonObject.optString("rspCode"));
				userEntity.setRspMsg(jsonObject.optString("rspMsg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return userEntity;
	}
}
