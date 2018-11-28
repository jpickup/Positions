package com.johnpickup.position;

import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PositionFileWriter {
    private final PositionCache positionCache;
    public void dumpPositions(File outputFile, long maxLadderDays) throws IOException {
        // Write a csv of the positions
        CSVWriter csvWriter = new CSVWriter(new FileWriter(outputFile));

        List<LocalDate> dates = new ArrayList<>(positionCache.distinctDates());
        dates.sort(LocalDate::compareTo);

        List<String> header = new ArrayList<>();
        header.add("book");
        header.add("isin");
        header.addAll(dates.stream().limit(maxLadderDays).map(d -> d.format(DateTimeFormatter.ISO_LOCAL_DATE)).collect(Collectors.toList()));
        csvWriter.writeNext(header.toArray(new String[]{}), false);

        List<PositionKey> positionKeys = new ArrayList<>(positionCache.ladderKeys());
        positionKeys.sort(null);
        for (PositionKey positionKey : positionKeys) {
            List<String> data = new ArrayList<>();
            data.add(positionKey.getBook().getName());
            data.add(positionKey.getInstrument().getIdentifier());
            dates.stream().limit(maxLadderDays).forEach(d -> data.add(positionCache.getPosition(positionKey, d).getNetPosition().toPlainString()));
            csvWriter.writeNext(data.toArray(new String[]{}), false);
        }

        csvWriter.flush();
        csvWriter.close();
    }
}
