package openassemblee.service;

import java.math.BigInteger;
import java.util.UUID;
import openassemblee.domain.ShortUid;
import openassemblee.repository.ShortUidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShortUidService {

    @Autowired
    private ShortUidRepository shortUidRepository;

    public static final BigInteger B = BigInteger.ONE.shiftLeft(64); // 2^64
    public static final BigInteger L = BigInteger.valueOf(Long.MAX_VALUE);

    public static BigInteger toBigInteger(UUID id) {
        BigInteger lo = BigInteger.valueOf(id.getLeastSignificantBits());
        BigInteger hi = BigInteger.valueOf(id.getMostSignificantBits());

        // If any of lo/hi parts is negative interpret as unsigned

        if (hi.signum() < 0) hi = hi.add(B);

        if (lo.signum() < 0) lo = lo.add(B);

        return lo.add(hi.multiply(B));
    }

    public static UUID fromBigInteger(BigInteger x) {
        BigInteger[] parts = x.divideAndRemainder(B);
        BigInteger hi = parts[0];
        BigInteger lo = parts[1];

        if (L.compareTo(lo) < 0) lo = lo.subtract(B);

        if (L.compareTo(hi) < 0) hi = hi.subtract(B);

        return new UUID(hi.longValueExact(), lo.longValueExact());
    }

    public ShortUid createShortUid() {
        UUID uuid = UUID.randomUUID();
        //        String uuidString = uuid.toString().replace("-", "");
        //        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        // méthode foireuse non ? deux uuids peuvent donner meme resultat ?
        //        bb.putLong(uuid.getMostSignificantBits());
        //        bb.putLong(uuid.getLeastSignificantBits());
        //        System.out.println(convertToBigInteger(uuid));
        //        System.out.println(new BigInteger(bb.array()));
        //        System.out.println("===");
        String uuidString = toBigInteger(uuid).toString();
        Long shortUuid = Long.parseLong(uuidString.substring(0, 3));
        if (shortUidRepository.findOneByUid(uuid.toString()) != null) {
            return createShortUid();
        }
        while (shortUidRepository.findOneByShortUid(shortUuid) != null) {
            shortUuid =
                Long.parseLong(
                    uuidString.substring(0, shortUuid.toString().length() + 1)
                );
        }
        ShortUid shortUid = new ShortUid(uuid.toString(), shortUuid);
        shortUidRepository.save(shortUid);
        return shortUid;
    }
}
