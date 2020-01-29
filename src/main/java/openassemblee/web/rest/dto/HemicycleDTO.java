package openassemblee.web.rest.dto;

import java.util.List;

public class HemicycleDTO {

    private List<HemicycleChairDTO> chairs;
    private int viewPortX;
    private int viewPortY;
    private int viewPortWidth;
    private int viewPortHeight;
    private int minChairNumber;
    private int maxChairNumber;

    public HemicycleDTO() {
    }

    public HemicycleDTO(List<HemicycleChairDTO> chairs, int viewPortX, int viewPortY, int viewPortWidth, int viewPortHeight, int minChairNumber, int maxChairNumber) {
        this.chairs = chairs;
        this.viewPortX = viewPortX;
        this.viewPortY = viewPortY;
        this.viewPortWidth = viewPortWidth;
        this.viewPortHeight = viewPortHeight;
        this.minChairNumber = minChairNumber;
        this.maxChairNumber = maxChairNumber;
    }

    public List<HemicycleChairDTO> getChairs() {
        return chairs;
    }

    public void setChairs(List<HemicycleChairDTO> chairs) {
        this.chairs = chairs;
    }

    public int getViewPortX() {
        return viewPortX;
    }

    public void setViewPortX(int viewPortX) {
        this.viewPortX = viewPortX;
    }

    public int getViewPortY() {
        return viewPortY;
    }

    public void setViewPortY(int viewPortY) {
        this.viewPortY = viewPortY;
    }

    public int getViewPortWidth() {
        return viewPortWidth;
    }

    public void setViewPortWidth(int viewPortWidth) {
        this.viewPortWidth = viewPortWidth;
    }

    public int getViewPortHeight() {
        return viewPortHeight;
    }

    public void setViewPortHeight(int viewPortHeight) {
        this.viewPortHeight = viewPortHeight;
    }

    public int getMinChairNumber() {
        return minChairNumber;
    }

    public void setMinChairNumber(int minChairNumber) {
        this.minChairNumber = minChairNumber;
    }

    public int getMaxChairNumber() {
        return maxChairNumber;
    }

    public void setMaxChairNumber(int maxChairNumber) {
        this.maxChairNumber = maxChairNumber;
    }
}
