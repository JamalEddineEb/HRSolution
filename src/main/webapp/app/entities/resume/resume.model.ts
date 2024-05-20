import { ICandidate } from 'app/entities/candidate/candidate.model';

export interface IResume {
  id: number;
  cv?: string | null;
  cvContentType?: string | null;
  owner?: ICandidate | null;
}

export type NewResume = Omit<IResume, 'id'> & { id: null };
