package sia5;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FluxCreationTests {

    //@Test
    public void createAFlux_just(){
        Flux<String>friutFlux=Flux.just(
                "Apple","Orange","Grape","Banana","Strawberry");
        friutFlux.subscribe(f->System.out.println("Here's some fruit: " + f));

        StepVerifier.create(friutFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    //@Test
    public void createAFlux_fromArray(){
        String[]fruits=new String[]{
                "Apple","Orange","Grape","Banana","Strawberry"
        };
        Flux<String> fruitAFlux=Flux.fromArray(fruits);
        //fruitAFlux.subscribe(f->System.out.println("Here's some fruit: " + f));
        StepVerifier.create(fruitAFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    //@Test
    public void fruitAFlux_fromIterable(){
        List<String> fruitList=new ArrayList<>();
        fruitList.add("Apple");
        fruitList.add("Orange");
        fruitList.add("Grape");
        fruitList.add("Banana");
        fruitList.add("Strawberry");

        Flux<String>stringFlux=Flux.fromIterable(fruitList);
        //stringFlux.subscribe(f->System.out.println("Here's some fruit: " + f));
        StepVerifier.create(stringFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }


    //@Test
    public void createAFlux_fromStream(){
        Stream<String>fruitString=Stream.of(
                "Apple","Orange","Grape","Banana","Strawberry");
        Flux<String>stringFlux=Flux.fromStream(fruitString);
       // stringFlux.subscribe(f->System.out.println("Here's some fruit: " + f));
        StepVerifier.create(stringFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    //@Test
    public void createAFlux_range(){
        Flux<Integer>intervalFlux=Flux.range(1,5);
        StepVerifier.create(intervalFlux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();
    }

    //@Test
    public void createAFlux_interval(){
        Flux<Long>intervalFlux=Flux.interval(Duration.ofSeconds(1))
                .take(5);
        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete();
    }

    //@Test
    public void mergeFluxes(){
        Flux<String>characterFlux=
                Flux.just("Garfield", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(300));
        Flux<String>foodFlux=Flux.just("Lasagna", "Lollipops", "Apples")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));
        Flux<String>mergedFlux=characterFlux.mergeWith(foodFlux);
        mergedFlux.subscribe(el-> System.out.println(el));
        //Flux<String>zippedFlux=Flux.zip(characterFlux,foodFlux,(c,f)->c+": "+f);

//        StepVerifier.create(mergedFlux)
//                .expectNext("Garfield")
//                .expectNext("Lasagna")
//                .expectNext("Kojak")
//                .expectNext("Lollipops")
//                .expectNext("Barbossa")
//                .expectNext("Apples")
//                .verifyComplete();
    }

    //@Test
    public void zipFluxes(){
        Flux<String>characterFlux=Flux.just("Garfield", "Kojak", "Barbossa");
        Flux<String>foodFlux=Flux.just("Lasagna", "Lollipops", "Apples");
        Flux<Tuple2<String,String>>zippedFlux=Flux.zip(characterFlux,foodFlux);

        StepVerifier.create(zippedFlux)
                .expectNextMatches(p->p.getT1().equals("Garfield")&&
                        p.getT2().equals("Lasagna"))
                .expectNextMatches(p->p.getT1().equals("Kojak")&&
                        p.getT2().equals("Lollipops"))
                .expectNextMatches(p->p.getT1().equals("Barbossa")&&
                        p.getT2().equals("Apples"))
                .verifyComplete();
    }

    //@Test
    public void zipfluxToObject(){
        Flux<String>characterFlux=Flux.just("Garfield", "Kojak", "Barbossa");
        Flux<String>foodFlux=Flux.just("Lasagna", "Lollipops", "Apples");

        Flux<String>zippedFlux=Flux.zip(characterFlux,foodFlux,(c,f)->c+" eats "+f);

        StepVerifier.create(zippedFlux)
                .expectNext("Garfield eats Lasagna")
                .expectNext("Kojak eats Lollipops")
                .expectNext("Barbossa eats Apples")
                .verifyComplete();

        zippedFlux.subscribe(i-> System.out.println(i));
    }

    //@Test
    public void firstFlux(){
        Flux<String>slowFlux=Flux.just("tortoise", "snail", "sloth")
                .delaySubscription(Duration.ofMillis(100));
        Flux<String>fastFlux=Flux.just("hare", "cheetah", "squirrel");

        Flux<String>firstFlux=Flux.first(slowFlux,fastFlux);

        //firstFlux.subscribe(i-> System.out.println("first: " + i));
        StepVerifier.create(firstFlux)
                .expectNext("hare")
                .expectNext("cheetah")
                .expectNext("squirrel")
                .verifyComplete();

    }

    //@Test
    public void skipAFew(){
        Flux<String>skipFlux=Flux.just(
                "one", "two", "skip a few", "ninety nine", "one hundred")
                .skip(3);
        StepVerifier.create(skipFlux)
                .expectNext("ninety nine", "one hundred")
                .verifyComplete();
        skipFlux.subscribe(i-> System.out.println("skip---"+i));
    }

    //@Test
    public void skipAFewSeconds(){
        Flux<String>skipFlux=Flux.just(
                "one", "two", "skip a few", "ninety nine", "one hundred")
                .delayElements(Duration.ofSeconds(1))
                .skip(Duration.ofSeconds(4));
//        StepVerifier.create(skipFlux)
//                .expectNext("ninety nine", "one hundred")
//                .verifyComplete();

        skipFlux.subscribe(i-> System.out.println(i));

    }
    //@Test
    public void take() {
        Flux<String> nationalParkFlux = Flux.just(
                "Yellowstone", "Yosemite", "Grand Canyon",
                "Zion", "Grand Teton").take(3);

        nationalParkFlux.subscribe(i-> System.out.println(i));
        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone", "Yosemite", "Grand Canyon")
                .verifyComplete();
    }

    @Test
    public void takeDelay() {
        Flux<String>nationalParkFlux=Flux.just(
                "Yellowstone", "Yosemite", "Grand Canyon",
                "Zion", "Grand Teton")
                .delayElements(Duration.ofMillis(10))
                .take(Duration.ofMillis(3500));
        nationalParkFlux.subscribe(i-> System.out.println(i));
    }
}
