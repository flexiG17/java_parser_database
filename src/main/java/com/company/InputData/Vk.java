package com.company.InputData;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.Fields;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Vk {

    //old d6aea7428ee98d5819bded443453d0d6b78446814dbc5c309fafa85974aa2ce7ea3b9f9ddc01aaa0ce7eb
    //new d624d40bc4b1f40ca0861a9d2712fe469df2cc56172afdd64ee08c7aa27e1ac6b225d1eb2c68010afc3a3
    private static final String ACCESS_TOKEN = "d624d40bc4b1f40ca0861a9d2712fe469df2cc56172afdd64ee08c7aa27e1ac6b225d1eb2c68010afc3a3";
    private static final int APP_ID = 8001584;
    private final VkApiClient vkClient;
    private final UserActor actor;
    private static String groupId;

    public Vk(String id){
        TransportClient transportClient = HttpTransportClient.getInstance();
        groupId = id;
        vkClient = new VkApiClient(transportClient);
        actor = new UserActor(APP_ID, ACCESS_TOKEN);
    }

    public List<JsonObject> find_users() throws ClientException, ApiException {
        var user_id = takeUsersId();
        var users = new ArrayList<JsonObject>(user_id.size());
        var counter = 0;
        while (counter < user_id.size()){
            var temp =
                    new JsonParser()
                            .parse(vkClient.users()
                                .get(actor)
                                .userIds(user_id.subList(counter, counter + (user_id.size()-counter) % 1000))
                            .fields(new Fields[]{Fields.BDATE,Fields.CITY,Fields.PHOTO_MAX,Fields.SEX}).execute().toString()).getAsJsonArray();

            temp.forEach(a -> users.add(a.getAsJsonObject()));
            counter += 1000;
        }
        return users;
    }

    private List<String> takeUsersId() throws ApiException, ClientException {
        var request = vkClient.groups().getMembers(actor).groupId(groupId);
        var allVkGroupUsers = new JsonParser().parse(request.execute().toString()).getAsJsonObject();

        var countOfUsers = allVkGroupUsers.get("count").getAsInt();
        var usersIds = allVkGroupUsers.get("items").getAsJsonArray();

        var allUsers = new HashSet<String>(countOfUsers);
        for (JsonElement usersId : usersIds) {
            allUsers.add(usersId.getAsString());
        }

        while (countOfUsers > 0){
            allVkGroupUsers = new JsonParser().parse(request.offset(1000).execute().toString()).getAsJsonObject();
            usersIds = allVkGroupUsers.get("items").getAsJsonArray();
            usersIds.forEach(a->allUsers.add(a.getAsString()));
            countOfUsers -= 1000;
        }
        return new ArrayList<>(allUsers);
    }
}
