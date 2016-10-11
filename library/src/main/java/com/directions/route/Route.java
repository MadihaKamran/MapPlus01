package com.directions.route;
//by Haseem Saheed

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class Route implements Parcelable {
    private String name;
    private final List<LatLng> points;
    private final List<Integer> durations;
    private List<Segment> segments;
    private String copyright;
    private String warning;
    private String country;
    private LatLngBounds latLgnBounds;
    private int length;
    private String polyline;
    private String durationText;
    private int durationValue;
    private String distanceText;
    private int distanceValue;
    private String endAddressText;
    private PolylineOptions polyOptions;

    public PolylineOptions getPolyOptions() {
        return polyOptions;
    }

    public void setPolyOptions(PolylineOptions polyOptions) {
        this.polyOptions = polyOptions;
    }

    public String getEndAddressText() {
        return endAddressText;
    }

    public void setEndAddressText(String endAddressText) {
        this.endAddressText = endAddressText;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }

    public int getDurationValue() {
        return durationValue;
    }

    public void setDurationValue(int durationValue) {
        this.durationValue = durationValue;
    }

    public int getDistanceValue() {
        return distanceValue;
    }

    public void setDistanceValue(int distanceValue) {
        this.distanceValue = distanceValue;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public Route() {
        points = new ArrayList<LatLng>();
        segments = new ArrayList<Segment>();
        durations = new ArrayList<Integer>();
    }



    public void addPoint(final LatLng p) {
        points.add(p);
    }

    public void addPoints(final List<LatLng> points) {
        this.points.addAll(points);
    }

    public void addDuration(int sec) {
        durations.add(sec);
    }
    public List<Integer> getDurations(){return durations;}

    public List<LatLng> getPoints() {
        return points;
    }

    public void addSegment(final Segment s) {
        segments.add(s);
    }

    public List<Segment> getSegments() {
        return segments;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param copyright the copyright to set
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * @return the copyright
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * @param warning the warning to set
     */
    public void setWarning(String warning) {
        this.warning = warning;
    }

    /**
     * @return the warning
     */
    public String getWarning() {
        return warning;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }


    /**
     * @param polyline the polyline to set
     */
    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    /**
     * @return the polyline
     */
    public String getPolyline() {
        return polyline;
    }

    /**
     * @return the LatLngBounds object to map camera
     */
    public LatLngBounds getLatLgnBounds() {
        return latLgnBounds;
    }


    public void setLatLgnBounds(LatLng northeast, LatLng southwest) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(northeast);
        builder.include(southwest);
        this.latLgnBounds = builder.build();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeTypedList(this.points);
        dest.writeList(this.durations);
        dest.writeList(this.segments);
        dest.writeString(this.copyright);
        dest.writeString(this.warning);
        dest.writeString(this.country);
        dest.writeParcelable(this.latLgnBounds, flags);
        dest.writeInt(this.length);
        dest.writeString(this.polyline);
        dest.writeString(this.durationText);
        dest.writeInt(this.durationValue);
        dest.writeString(this.distanceText);
        dest.writeInt(this.distanceValue);
        dest.writeString(this.endAddressText);
        dest.writeParcelable(this.polyOptions, flags);
    }

    protected Route(Parcel in) {
        this.name = in.readString();
        this.points = in.createTypedArrayList(LatLng.CREATOR);
        this.durations = new ArrayList<Integer>();
        in.readList(this.durations, Integer.class.getClassLoader());
        this.segments = new ArrayList<Segment>();
        in.readList(this.segments, Segment.class.getClassLoader());
        this.copyright = in.readString();
        this.warning = in.readString();
        this.country = in.readString();
        this.latLgnBounds = in.readParcelable(LatLngBounds.class.getClassLoader());
        this.length = in.readInt();
        this.polyline = in.readString();
        this.durationText = in.readString();
        this.durationValue = in.readInt();
        this.distanceText = in.readString();
        this.distanceValue = in.readInt();
        this.endAddressText = in.readString();
        this.polyOptions = in.readParcelable(PolylineOptions.class.getClassLoader());
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel source) {
            return new Route(source);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };
}

