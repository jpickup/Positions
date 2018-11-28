package com.johnpickup.jmx;

import com.johnpickup.data.Book;
import com.johnpickup.data.Instrument;
import com.johnpickup.position.Position;
import com.johnpickup.position.PositionCache;
import com.johnpickup.position.PositionCategory;
import com.johnpickup.position.PositionFileWriter;
import com.johnpickup.position.PositionKey;
import com.johnpickup.position.PositionLadder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@ManagedResource(objectName = "Positions:type=jmx,name=positionServerManagement", description="Position Server Management Interface")
public class PositionServerManagement {
    private final PositionFileWriter positionFileWriter;
    private final PositionCache positionCache;
    private static final DecimalFormat positionFormat = new DecimalFormat("#,##0");

    @ManagedOperation(description = "Republish all positions to subscribers")
    public void republishAll() {
        positionCache.broadcastAll();
    }

    @ManagedOperation(description = "Display all the book/instrument keys in the server")
    public String positionKeys() {
        return  "<table>" +
                "<tr><th>Book</th><th>Instrument</th></tr>" +
                positionCache.ladderKeys().stream()
                        .sorted()
                        .map(this::positionKeyTableRow)
                        .collect(Collectors.joining("\n")) +
                "</table>";
    }

    private String positionKeyTableRow(PositionKey k) {
        return "<tr>" +
                "<td>" + k.getBook().getName() + "</td>" +
                "<td>" + k.getInstrument().getIdentifier() + "</td>" +
                "</tr>";
    }

    @ManagedOperation(description = "Save the current position cache to a csv file")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name="filename", description="The name of the file to write"),
            @ManagedOperationParameter(name="maxLadderDays", description="The maximum number of dates")
    })
    public void dumpPositionsToFile(String filename, int maxLadderDays) {
        try {
            positionFileWriter.dumpPositions(new File(filename), maxLadderDays);
        }
        catch (IOException e) {
            log.warn("Error dumping positions", e);
            throw new RuntimeException(e);
        }
    }

    @ManagedOperation(description = "Display the details of a single book/instrument position over all dates")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name="book", description="The book code"),
            @ManagedOperationParameter(name="instrument", description="The instrument (ISIN)")
    })
    public String dumpPosition(String book, String instrument) {
        PositionKey positionKey = new PositionKey(new Book(book), new Instrument(instrument));
        PositionLadder ladder = positionCache.getExistingPositionLadder(positionKey);

        if (ladder == null) {
            return "No position ladder found for " + positionKey;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        sb.append("<tr><th>Book</th><th>Instrument</th><th>Date</th><th>Net Position</th><th>Bond Position</th><th>Repo Position</th><th>Sec Lend Position</th><th>Reservation</th></tr>");
        for (LocalDate date : ladder.dates()) {
            Position position = ladder.getPosition(date);
            sb.append("<tr>")
                    .append("<td>").append(positionKey.getBook().getName()).append("</td>")
                    .append("<td>").append(positionKey.getInstrument().getIdentifier()).append("</td>")
                    .append("<td>").append(date.format(DateTimeFormatter.ISO_LOCAL_DATE)).append("</td>")
                    .append("<td>").append(positionFormat.format(position.getNetPosition())).append("</td>")
                    .append("<td>").append(positionFormat.format(position.getPositionComponent(PositionCategory.BOND))).append("</td>")
                    .append("<td>").append(positionFormat.format(position.getPositionComponent(PositionCategory.REPO))).append("</td>")
                    .append("<td>").append(positionFormat.format(position.getPositionComponent(PositionCategory.SEC_LEND))).append("</td>")
                    .append("<td>").append(positionFormat.format(position.getPositionComponent(PositionCategory.RESERVATION))).append("</td>")
                    .append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    @ManagedOperation(description = "Display all positions out to a maximum number of days")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name="maxLadderDays", description="The maximum number of dates to display")
    })
    public String dumpPositions(int maxLadderDays) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        sb.append("<tr><th>Book</th><th>Instrument</th>");
        List<LocalDate> dates = positionCache.distinctDates().stream().sorted().limit(maxLadderDays).collect(Collectors.toList());
        dates.forEach(d -> sb.append("<th>")
                .append(d.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .append("</th>"));
        sb.append("</tr>");

        for (PositionKey positionKey : positionCache.ladderKeys().stream().sorted().collect(Collectors.toList())) {
            sb.append(String.format("<tr><td>%s</td><td>%s</td>", positionKey.getBook().getName(), positionKey.getInstrument().getIdentifier()));
            dates.forEach(d -> sb.append("<td align='right'>")
                    .append(positionFormat.format(positionCache.getPosition(positionKey, d).getNetPosition()))
                    .append("</td>"));
            sb.append("</tr>");
        }
        sb.append("</table>");

        return sb.toString();
    }
}
