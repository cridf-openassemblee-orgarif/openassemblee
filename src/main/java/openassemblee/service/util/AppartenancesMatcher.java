package openassemblee.service.util;

import openassemblee.domain.*;

public class AppartenancesMatcher {

    public static Boolean match(
        AppartenanceGroupePolitique a,
        FonctionGroupePolitique f
    ) {
        if (a.getGroupePolitique() == null || f.getGroupePolitique() == null) {
            return false;
        }
        if (
            !a
                .getGroupePolitique()
                .getId()
                .equals(f.getGroupePolitique().getId())
        ) {
            return false;
        }
        if (
            a.getDateDebut() != null &&
            f.getDateDebut() != null &&
            a.getDateDebut().isAfter(f.getDateDebut())
        ) {
            return false;
        }
        if (
            a.getDateFin() != null &&
            f.getDateFin() != null &&
            a.getDateFin().isBefore(f.getDateFin())
        ) {
            return false;
        }
        return true;
    }

    public static Boolean match(
        AppartenanceCommissionThematique a,
        FonctionCommissionThematique f
    ) {
        if (
            a.getCommissionThematique() == null ||
            f.getCommissionThematique() == null
        ) {
            return false;
        }
        if (
            !a
                .getCommissionThematique()
                .getId()
                .equals(f.getCommissionThematique().getId())
        ) {
            return false;
        }
        if (
            a.getDateDebut() != null &&
            f.getDateDebut() != null &&
            a.getDateDebut().isAfter(f.getDateDebut())
        ) {
            return false;
        }
        if (
            a.getDateFin() != null &&
            f.getDateFin() != null &&
            a.getDateFin().isBefore(f.getDateFin())
        ) {
            return false;
        }
        return true;
    }

    public static Boolean match(Mandat a, FonctionExecutive f) {
        if (a.getMandature() == null || f.getMandature() == null) {
            return false;
        }
        if (!a.getMandature().getId().equals(f.getMandature().getId())) {
            return false;
        }
        if (
            a.getDateDebut() != null &&
            f.getDateDebut() != null &&
            a.getDateDebut().isAfter(f.getDateDebut())
        ) {
            return false;
        }
        if (
            a.getDateDemission() != null &&
            f.getDateFin() != null &&
            a.getDateDemission().isBefore(f.getDateFin())
        ) {
            return false;
        }
        return true;
    }

    public static Boolean match(Mandat a, AppartenanceCommissionPermanente f) {
        if (a.getMandature() == null || f.getMandature() == null) {
            return false;
        }
        if (!a.getMandature().getId().equals(f.getMandature().getId())) {
            return false;
        }
        if (
            a.getDateDebut() != null &&
            f.getDateDebut() != null &&
            a.getDateDebut().isAfter(f.getDateDebut())
        ) {
            return false;
        }
        if (
            a.getDateDemission() != null &&
            f.getDateFin() != null &&
            a.getDateDemission().isBefore(f.getDateFin())
        ) {
            return false;
        }
        return true;
    }

    public static Boolean match(Mandat a, AutreMandat f) {
        if (a.getMandature() == null || f.getMandature() == null) {
            return false;
        }
        if (!a.getMandature().getId().equals(f.getMandature().getId())) {
            return false;
        }
        if (
            a.getDateDebut() != null &&
            f.getDateDebut() != null &&
            a.getDateDebut().isAfter(f.getDateDebut())
        ) {
            return false;
        }
        if (
            a.getDateDemission() != null &&
            f.getDateFin() != null &&
            a.getDateDemission().isBefore(f.getDateFin())
        ) {
            return false;
        }
        return true;
    }

    public static Boolean match(Mandat a, FonctionCommissionPermanente f) {
        if (a.getMandature() == null || f.getMandature() == null) {
            return false;
        }
        if (!a.getMandature().getId().equals(f.getMandature().getId())) {
            return false;
        }
        if (
            a.getDateDebut() != null &&
            f.getDateDebut() != null &&
            a.getDateDebut().isAfter(f.getDateDebut())
        ) {
            return false;
        }
        if (
            a.getDateDemission() != null &&
            f.getDateFin() != null &&
            a.getDateDemission().isBefore(f.getDateFin())
        ) {
            return false;
        }
        return true;
    }
}
