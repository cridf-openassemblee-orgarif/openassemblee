package openassemblee.service.dto;

import openassemblee.domain.CommissionThematique;

public class CommissionThematiqueListDTO {

    private CommissionThematique commissionThematique;
    private int count;

    public CommissionThematiqueListDTO(CommissionThematique commissionThematique, int count) {
        this.commissionThematique = commissionThematique;
        this.count = count;
    }

    public CommissionThematique getCommissionThematique() {
        return commissionThematique;
    }

    public int getCount() {
        return count;
    }
}
