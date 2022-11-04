package io.collective.start;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.collective.articles.ArticleDataGateway;
import io.collective.articles.ArticleRecord;
import io.collective.articles.ArticlesController;
import io.collective.endpoints.EndpointTask;
import io.collective.restsupport.BasicApp;
import io.collective.restsupport.NoopController;
import io.collective.workflow.*;
import org.eclipse.jetty.server.handler.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;


public class App extends BasicApp {
    private ArticleDataGateway dataGateway;

    @Override
    public void start() {
        super.start();

        { // todo - start the endpoint worker
            WorkFinder<EndpointTask> finder = new WorkFinder<EndpointTask>() {
                @Override
                public void markCompleted(EndpointTask info) {

                }

                @NotNull
                @Override
                public List findRequested(@NotNull String name) {
                    return null;
                }
            };
            Worker<EndpointTask> worker = new Worker<EndpointTask>() {
                @Override
                public void execute(EndpointTask task) throws Exception {

                }

                @NotNull
                @Override
                public String getName() {
                    return null;
                }

            };

//            WorkScheduler<EndpointTask> scheduler = new WorkScheduler<>(finder, worker, 300);
        }
    }

    public App(int port) {
        super(port);
    }

    @NotNull
    @Override
    protected HandlerList handlerList() {
        dataGateway = new ArticleDataGateway(List.of(
                new ArticleRecord(10101, "Programming Languages InfoQ Trends Report - October 2019 4", true),
                new ArticleRecord(10106, "Ryan Kitchens on Learning from Incidents at Netflix, the Role of SRE, and Sociotechnical Systems", true)
        ));

        HandlerList list = new HandlerList();
        list.addHandler(new ArticlesController(new ObjectMapper(), dataGateway));
        list.addHandler(new NoopController());
        return list;
    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        String port = System.getenv("PORT") != null ? System.getenv("PORT") : "8881";
        App app = new App(Integer.parseInt(port));
        app.start();
    }
}
