package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.*;

@ExtendWith(UsersQueueExtension.class)
public class ProfileQueueTest {

    //первая часть домашней работы, возможно понадобится еще
//    @Test
//    void testWithEmptyUser1(@UserType(empty = true) StaticUser user0,
//                            @UserType(empty = false) StaticUser user1) throws InterruptedException {
//        Thread.sleep(1000);
//        System.out.println(user0);
//        System.out.println(user1);
//    }
//
//    @Test
//    void testWithEmptyUser2(@UserType(empty = true) StaticUser user0,
//                            @UserType(empty = false) StaticUser user1) throws InterruptedException {
//        Thread.sleep(1000);
//        System.out.println(user0);
//        System.out.println(user1);
//    }
//
//    @Test
//    void testWithEmptyUser(@UserType(empty = true) StaticUser user0,
//                           @UserType(empty = false) StaticUser user1) throws InterruptedException {
//        Thread.sleep(1000);
//        System.out.println(user0);
//        System.out.println(user1);
//    }
}
