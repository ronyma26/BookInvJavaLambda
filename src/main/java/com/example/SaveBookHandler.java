package com.example;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.HashMap;
import java.util.Map;

public class SaveBookHandler implements RequestHandler<BookInfoRequest, BookInfoResponse> {

    private AmazonDynamoDB amazonDynamoDB;

    private String DYNAMODB_TABLE_NAME = "Booking";
    private Regions REGION = Regions.EU_NORTH_1;

    public BookInfoResponse handleRequest(BookInfoRequest bookInfoRequest, Context context) {
        this.initDynamoDbClient();

        persistData(bookInfoRequest);

        BookInfoResponse bookInfoResponse = new BookInfoResponse();
        bookInfoResponse.setMessage("Saved Successfully!!!");
        return bookInfoResponse;
    }

    private void persistData(BookInfoRequest bookInfoRequest) throws ConditionalCheckFailedException {

        Map<String, AttributeValue> attributesMap = new HashMap<>();


        attributesMap.put("id", new AttributeValue(String.valueOf(bookInfoRequest.getId())));
        attributesMap.put("title", new AttributeValue(bookInfoRequest.getTitle()));
        attributesMap.put("isbn", new AttributeValue(bookInfoRequest.getIsbn()));
        attributesMap.put("author", new AttributeValue(bookInfoRequest.getAuthor()));
        attributesMap.put("description", new AttributeValue(bookInfoRequest.getDescription()));

        amazonDynamoDB.putItem(DYNAMODB_TABLE_NAME, attributesMap);
    }

    private void initDynamoDbClient() {
        this.amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withRegion(REGION)
                .build();
    }
}
