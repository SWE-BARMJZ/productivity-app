import React, { useContext } from "react";
import { VStack } from "native-base";
import AuthContext from "./store/auth-context";

import { NavigationContainer } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import { createDrawerNavigator } from "@react-navigation/drawer";

const Stack = createNativeStackNavigator();
const Drawer = createDrawerNavigator();

import { Home } from "./components/Home";
import { Login } from "./components/Login";
import { PasswordRecovery } from "./components/PasswordRecovery";
import { SignUp } from "./components/SignUp";
import { Notes } from "./components/notes-component/Notes-Main";
import TodoScreen from "./components/TodoScreen";
import MindMapScreen from "./components/MindMapScreen";

const AppNavigation = () => {
  const auth = useContext(AuthContext);

  return (
    <NavigationContainer>
      {auth.isLoggedIn ? (
        <Drawer.Navigator
          screenOptions={{ headerShown: false }}
          drawerContent={CustomDrawerContent}
        >
          <Drawer.Screen name="Home" component={Home} />
          <Drawer.Screen name="Mindmap" component={MindMapScreen} />
          <Drawer.Screen name="Todo" component={TodoScreen} />
          <Drawer.Screen name="Notes" component={Notes} />
        </Drawer.Navigator>
      ) : (
        <Stack.Navigator screenOptions={{ headerShown: false }}>
          <Stack.Screen name="Login" component={Login} />
          <Stack.Screen name="Sign Up" component={SignUp} />
          <Stack.Screen name="Password Recovery" component={PasswordRecovery} />
        </Stack.Navigator>
      )}
    </NavigationContainer>
  );
};

import {
  DrawerContentScrollView,
  DrawerItemList,
  DrawerItem,
} from "@react-navigation/drawer";
import { Ionicons } from "@expo/vector-icons";

function CustomDrawerContent(props) {
  const auth = useContext(AuthContext);

  return (
    <VStack justifyContent="space-between" h={"full"}>
      <DrawerContentScrollView {...props}>
        <DrawerItemList {...props} />
      </DrawerContentScrollView>

      <DrawerItem
        label={"Sign out"}
        icon={() => <Ionicons name={"exit-outline"} size={22} />}
        labelStyle={{ marginLeft: -25 }}
        onPress={() => auth.logout()}
      />
    </VStack>
  );
}
export default AppNavigation;