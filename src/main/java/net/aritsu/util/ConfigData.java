package net.aritsu.util;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigData {

    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static boolean exampleClientBoolean = false;
    public static int exampleServerInteger = 0;


    //server
    public static int cookMarshmallows = 15;

    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static void refreshClient() {
        exampleClientBoolean = CLIENT.booleanSample.get();
    }

    public static void refreshServer() {
        exampleServerInteger = SERVER.intValue.get();
        cookMarshmallows = SERVER.marshmallowCookTime.get();

    }

    public static class ClientConfig {

        public final ForgeConfigSpec.BooleanValue booleanSample;

        ClientConfig(ForgeConfigSpec.Builder builder) {

            builder.push("general");
            booleanSample = builder.comment("Pick true to show a larger model in the wardrobe screen").translation("translate.show.size").define("bigger_model", true);
            builder.pop();

        }
    }

    public static class ServerConfig {
        public final ForgeConfigSpec.IntValue intValue;
        public final ForgeConfigSpec.IntValue marshmallowCookTime;

        ServerConfig(ForgeConfigSpec.Builder builder) {

            builder.push("general");
            intValue = builder.comment("a sample int value").translation("int.value.sample").defineInRange("sample_value", 0, -10, 100);
            marshmallowCookTime = builder.comment("one chance in x to cook a marshmallow every second").translation("cook.marshmallow.timer").defineInRange("marshmallowCookTimer", 15, 1, 10000);
            builder.pop();
        }
    }
}